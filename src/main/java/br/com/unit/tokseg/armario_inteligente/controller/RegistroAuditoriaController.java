package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.RegistroAuditoria;
import br.com.unit.tokseg.armario_inteligente.service.RegistroAuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller responsável por gerenciar as operações relacionadas aos registros de auditoria.
 * Expõe endpoints REST para criar, listar, buscar e remover registros.
 * 
 * Endpoints disponíveis:
 * - GET /api/auditoria: Lista todos os registros
 * - GET /api/auditoria/{id}: Busca um registro específico
 * - POST /api/auditoria: Cria um novo registro
 * - DELETE /api/auditoria/{id}: Remove um registro
 * 
 * Todos os endpoints requerem autenticação e autorização adequada.
 */
@RestController
@RequestMapping("/api/auditoria")
@Tag(name = "Auditoria", description = "API para gerenciamento de registros de auditoria")
@PreAuthorize("hasRole('ADMIN')")
public class RegistroAuditoriaController {

    private final RegistroAuditoriaService registroAuditoriaService;

    /**
     * Construtor que recebe o serviço de registros de auditoria via injeção de dependência.
     * O Spring Boot gerencia automaticamente a criação e injeção do serviço.
     */
    public RegistroAuditoriaController(RegistroAuditoriaService registroAuditoriaService) {
        this.registroAuditoriaService = registroAuditoriaService;
    }

    @Operation(summary = "Lista todos os registros de auditoria", description = "Retorna uma lista de todos os registros de auditoria cadastrados no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de registros retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<List<RegistroAuditoria>> listarTodos() {
        return ResponseEntity.ok(registroAuditoriaService.listarTodos());
    }

    @Operation(summary = "Busca um registro de auditoria por ID", description = "Retorna um registro específico baseado no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RegistroAuditoria> buscarPorId(
            @Parameter(description = "ID do registro a ser buscado", required = true)
            @PathVariable UUID id) {
        return registroAuditoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo registro de auditoria", description = "Cria um novo registro de auditoria no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<RegistroAuditoria> criar(
            @Parameter(description = "Dados do registro a ser criado", required = true)
            @RequestBody RegistroAuditoria registro) {
        try {
            return ResponseEntity.ok(registroAuditoriaService.salvar(registro));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Remove um registro de auditoria", description = "Remove um registro de auditoria do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Registro removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do registro a ser removido", required = true)
            @PathVariable UUID id) {
        try {
            registroAuditoriaService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
