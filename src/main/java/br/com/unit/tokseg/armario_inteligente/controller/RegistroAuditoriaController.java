package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.RegistroAuditoria;
import br.com.unit.tokseg.armario_inteligente.service.RegistroAuditoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * Lista todos os registros de auditoria cadastrados no sistema.
     * 
     * @return Lista de todos os registros
     */
    @GetMapping
    public ResponseEntity<List<RegistroAuditoria>> listarTodos() {
        return ResponseEntity.ok(registroAuditoriaService.listarTodos());
    }

    /**
     * Busca um registro específico pelo ID.
     * 
     * @param id ID do registro a ser buscado
     * @return Registro encontrado ou erro 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<RegistroAuditoria> buscarPorId(@PathVariable Long id) {
        return registroAuditoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo registro de auditoria no sistema.
     * 
     * @param registro Dados do registro a ser criado
     * @return Registro criado ou erro 400 se os dados forem inválidos
     */
    @PostMapping
    public ResponseEntity<RegistroAuditoria> criar(@RequestBody RegistroAuditoria registro) {
        try {
            return ResponseEntity.ok(registroAuditoriaService.salvar(registro));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Remove um registro de auditoria do sistema.
     * 
     * @param id ID do registro a ser removido
     * @return ResponseEntity sem conteúdo (204) se removido com sucesso, ou erro 404 se não encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            registroAuditoriaService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
