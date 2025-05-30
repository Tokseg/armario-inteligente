package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.Compartimento;
import br.com.unit.tokseg.armario_inteligente.service.CompartimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por gerenciar as operações relacionadas aos compartimentos dos armários.
 * Expõe endpoints REST para criar, listar, buscar e remover compartimentos.
 * 
 * Endpoints disponíveis:
 * - GET /api/compartimentos: Lista todos os compartimentos (AUTENTICADO)
 * - GET /api/compartimentos/{id}: Busca um compartimento específico (AUTENTICADO)
 * - POST /api/compartimentos: Cria um novo compartimento (ADMIN)
 * - DELETE /api/compartimentos/{id}: Remove um compartimento (ADMIN)
 */
@RestController
@RequestMapping("/api/compartimentos")
public class CompartimentoController {

    private final CompartimentoService compartimentoService;

    /**
     * Construtor que recebe o serviço de compartimentos via injeção de dependência.
     * 
     * @param compartimentoService Serviço de compartimentos a ser injetado
     */
    public CompartimentoController(CompartimentoService compartimentoService) {
        this.compartimentoService = compartimentoService;
    }

    /**
     * Lista todos os compartimentos cadastrados no sistema.
     * Requer autenticação.
     * 
     * @return Lista de todos os compartimentos
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Compartimento>> listarTodos() {
        return ResponseEntity.ok(compartimentoService.findAll());
    }

    /**
     * Busca um compartimento específico pelo ID.
     * Requer autenticação.
     * 
     * @param id ID do compartimento a ser buscado
     * @return Compartimento encontrado ou erro 404 se não existir
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Compartimento> buscarPorId(@PathVariable Long id) {
        return compartimentoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo compartimento no sistema.
     * Requer permissão de ADMIN.
     * 
     * @param compartimento Dados do compartimento a ser criado
     * @return Compartimento criado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Compartimento> criar(@RequestBody Compartimento compartimento) {
        return ResponseEntity.ok(compartimentoService.save(compartimento));
    }

    /**
     * Remove um compartimento do sistema.
     * Requer permissão de ADMIN.
     * 
     * @param id ID do compartimento a ser removido
     * @return ResponseEntity sem conteúdo (204) se removido com sucesso
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        compartimentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 