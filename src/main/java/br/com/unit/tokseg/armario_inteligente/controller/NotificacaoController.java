package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.Notificacao;
import br.com.unit.tokseg.armario_inteligente.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por gerenciar as operações relacionadas às notificações do sistema.
 * Expõe endpoints REST para criar, listar, buscar e remover notificações.
 * 
 * Endpoints disponíveis:
 * - GET /api/notificacoes: Lista todas as notificações (AUTENTICADO)
 * - GET /api/notificacoes/{id}: Busca uma notificação específica (AUTENTICADO)
 * - POST /api/notificacoes: Cria uma nova notificação (ADMIN)
 * - DELETE /api/notificacoes/{id}: Remove uma notificação (ADMIN)
 */
@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    /**
     * Construtor que recebe o serviço de notificações via injeção de dependência.
     * 
     * @param notificacaoService Serviço de notificações a ser injetado
     */
    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    /**
     * Lista todas as notificações cadastradas no sistema.
     * Requer autenticação.
     * 
     * @return Lista de todas as notificações
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notificacao>> listarTodas() {
        return ResponseEntity.ok(notificacaoService.listarTodas());
    }

    /**
     * Busca uma notificação específica pelo ID.
     * Requer autenticação.
     * 
     * @param id ID da notificação a ser buscada
     * @return Notificação encontrada ou erro 404 se não existir
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Notificacao> buscarPorId(@PathVariable String id) {
        return notificacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova notificação no sistema.
     * Requer permissão de ADMIN.
     * 
     * @param notificacao Dados da notificação a ser criada
     * @return Notificação criada
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Notificacao> criar(@RequestBody Notificacao notificacao) {
        return ResponseEntity.ok(notificacaoService.salvar(notificacao));
    }

    /**
     * Remove uma notificação do sistema.
     * Requer permissão de ADMIN.
     * 
     * @param id ID da notificação a ser removida
     * @return ResponseEntity sem conteúdo (204) se removida com sucesso
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        notificacaoService.remover(id);
        return ResponseEntity.noContent().build();
    }
} 