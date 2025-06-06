package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.exception.ResourceNotFoundException;
import br.com.unit.tokseg.armario_inteligente.model.Encomenda;
import br.com.unit.tokseg.armario_inteligente.service.EncomendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller responsável por gerenciar as operações relacionadas às encomendas.
 * Expõe endpoints REST para criar, listar, buscar, atualizar e remover encomendas.
 * 
 * Endpoints disponíveis:
 * - GET /api/encomendas: Lista todas as encomendas (AUTENTICADO)
 * - GET /api/encomendas/{id}: Busca uma encomenda específica (AUTENTICADO)
 * - GET /api/encomendas/usuario/{usuarioId}: Lista encomendas de um usuário (AUTENTICADO)
 * - GET /api/encomendas/armario/{armarioId}: Lista encomendas de um armário (AUTENTICADO)
 * - POST /api/encomendas: Cria uma nova encomenda (AUTENTICADO)
 * - PUT /api/encomendas/{id}: Atualiza uma encomenda (ADMIN)
 * - PATCH /api/encomendas/{id}/retirada: Confirma retirada de encomenda (AUTENTICADO)
 * - DELETE /api/encomendas/{id}: Remove uma encomenda (ADMIN)
 */
@RestController
@RequestMapping("/api/encomendas")
@Tag(name = "Encomendas", description = "API para gerenciamento de encomendas")
@SecurityRequirement(name = "bearerAuth")
public class EncomendaController {

    private final EncomendaService encomendaService;

    /**
     * Construtor que recebe o serviço de encomendas via injeção de dependência.
     * 
     * @param encomendaService Serviço de encomendas a ser injetado
     */
    public EncomendaController(EncomendaService encomendaService) {
        this.encomendaService = encomendaService;
    }

    @Operation(summary = "Lista todas as encomendas", description = "Retorna uma lista de todas as encomendas cadastradas no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de encomendas retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Encomenda.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Encomenda>> listarTodas() {
        return ResponseEntity.ok(encomendaService.listarTodas());
    }

    @Operation(summary = "Busca uma encomenda por ID", description = "Retorna uma encomenda específica baseada no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Encomenda encontrada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Encomenda.class))),
        @ApiResponse(responseCode = "404", description = "Encomenda não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Encomenda> buscarPorId(
            @Parameter(description = "ID da encomenda a ser buscada", required = true)
            @PathVariable UUID id) {
        return encomendaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Encomenda", "id", id));
    }

    @Operation(summary = "Lista encomendas por usuário", description = "Retorna uma lista de encomendas de um usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de encomendas retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Encomenda.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Encomenda>> buscarPorUsuario(
            @Parameter(description = "ID do usuário cujas encomendas serão listadas", required = true)
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(encomendaService.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Lista encomendas por armário", description = "Retorna uma lista de encomendas de um armário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de encomendas retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Encomenda.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/armario/{armarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Encomenda>> buscarPorArmario(
            @Parameter(description = "ID do armário cujas encomendas serão listadas", required = true)
            @PathVariable UUID armarioId) {
        return ResponseEntity.ok(encomendaService.buscarPorArmario(armarioId));
    }

    @Operation(summary = "Cria uma nova encomenda", description = "Cria uma nova encomenda no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Encomenda criada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Encomenda.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Encomenda> criar(
            @Parameter(description = "Dados da encomenda a ser criada", required = true)
            @Valid @RequestBody Encomenda encomenda) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(encomendaService.salvar(encomenda));
    }

    @Operation(summary = "Atualiza uma encomenda", description = "Atualiza os dados de uma encomenda existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Encomenda atualizada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Encomenda.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Encomenda não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Encomenda> atualizar(
            @Parameter(description = "ID da encomenda a ser atualizada", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Dados da encomenda atualizada", required = true)
            @Valid @RequestBody Encomenda encomenda) {
        if (!encomendaService.existePorId(id)) {
            throw new ResourceNotFoundException("Encomenda", "id", id);
        }
        encomenda.setId(id);
        return ResponseEntity.ok(encomendaService.salvar(encomenda));
    }

    @Operation(summary = "Confirma retirada de encomenda", description = "Marca uma encomenda como retirada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retirada confirmada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Encomenda.class))),
        @ApiResponse(responseCode = "404", description = "Encomenda não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PatchMapping("/{id}/retirada")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Encomenda> confirmarRetirada(
            @Parameter(description = "ID da encomenda a ser confirmada", required = true)
            @PathVariable UUID id) {
        return encomendaService.confirmarRetirada(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Encomenda", "id", id));
    }

    @Operation(summary = "Remove uma encomenda", description = "Remove uma encomenda do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Encomenda removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Encomenda não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID da encomenda a ser removida", required = true)
            @PathVariable UUID id) {
        if (!encomendaService.existePorId(id)) {
            throw new ResourceNotFoundException("Encomenda", "id", id);
        }
        encomendaService.remover(id);
        return ResponseEntity.noContent().build();
    }
} 