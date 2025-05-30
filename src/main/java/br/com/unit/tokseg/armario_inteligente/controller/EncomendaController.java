package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.Encomenda;
import br.com.unit.tokseg.armario_inteligente.service.EncomendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por gerenciar as operações relacionadas às encomendas.
 * Expõe endpoints REST para criar, listar, buscar e remover encomendas.
 * 
 * Endpoints disponíveis:
 * - GET /api/encomendas: Lista todas as encomendas (AUTENTICADO)
 * - GET /api/encomendas/{id}: Busca uma encomenda específica (AUTENTICADO)
 * - POST /api/encomendas: Cria uma nova encomenda (AUTENTICADO)
 * - DELETE /api/encomendas/{id}: Remove uma encomenda (ADMIN)
 */
@RestController
@RequestMapping("/api/encomendas")
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

    /**
     * Lista todas as encomendas cadastradas no sistema.
     * Requer autenticação.
     * 
     * @return Lista de todas as encomendas
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Encomenda>> listarTodas() {
        return ResponseEntity.ok(encomendaService.listarTodas());
    }

    /**
     * Busca uma encomenda específica pelo ID.
     * Requer autenticação.
     * 
     * @param id ID da encomenda a ser buscada
     * @return Encomenda encontrada ou erro 404 se não existir
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Encomenda> buscarPorId(@PathVariable String id) {
        return encomendaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova encomenda no sistema.
     * Requer autenticação.
     * 
     * @param encomenda Dados da encomenda a ser criada
     * @return Encomenda criada
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Encomenda> criar(@RequestBody Encomenda encomenda) {
        return ResponseEntity.ok(encomendaService.salvar(encomenda));
    }

    /**
     * Remove uma encomenda do sistema.
     * Requer permissão de ADMIN.
     * 
     * @param id ID da encomenda a ser removida
     * @return ResponseEntity sem conteúdo (204) se removida com sucesso
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        encomendaService.remover(id);
        return ResponseEntity.noContent().build();
    }
} 