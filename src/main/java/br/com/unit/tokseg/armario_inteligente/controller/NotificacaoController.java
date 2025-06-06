package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.exception.ResourceNotFoundException;
import br.com.unit.tokseg.armario_inteligente.model.Notificacao;
import br.com.unit.tokseg.armario_inteligente.service.NotificacaoService;
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
 * Controller responsável por gerenciar as operações relacionadas às notificações.
 * Expõe endpoints REST para criar, listar, buscar, atualizar e remover notificações.
 * 
 * Endpoints disponíveis:
 * - GET /api/notificacoes: Lista todas as notificações (AUTENTICADO)
 * - GET /api/notificacoes/{id}: Busca uma notificação específica (AUTENTICADO)
 * - GET /api/notificacoes/usuario/{usuarioId}: Lista notificações de um usuário (AUTENTICADO)
 * - GET /api/notificacoes/usuario/{usuarioId}/nao-lidas: Lista notificações não lidas (AUTENTICADO)
 * - POST /api/notificacoes: Cria uma nova notificação (ADMIN)
 * - PATCH /api/notificacoes/{id}/ler: Marca notificação como lida (AUTENTICADO)
 * - DELETE /api/notificacoes/{id}: Remove uma notificação (ADMIN)
 */
@RestController
@RequestMapping("/api/notificacoes")
@Tag(name = "Notificações", description = "API para gerenciamento de notificações")
@SecurityRequirement(name = "bearerAuth")
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

    @Operation(summary = "Lista todas as notificações", description = "Retorna uma lista de todas as notificações cadastradas no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Notificacao.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Notificacao>> listarTodas() {
        return ResponseEntity.ok(notificacaoService.listarTodas());
    }

    @Operation(summary = "Busca uma notificação por ID", description = "Retorna uma notificação específica baseada no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificação encontrada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Notificacao.class))),
        @ApiResponse(responseCode = "404", description = "Notificação não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Notificacao> buscarPorId(
            @Parameter(description = "ID da notificação a ser buscada", required = true)
            @PathVariable UUID id) {
        return notificacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Notificacao", "id", id));
    }

    @Operation(summary = "Lista notificações por usuário", description = "Retorna uma lista de notificações de um usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Notificacao.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notificacao>> buscarPorUsuario(
            @Parameter(description = "ID do usuário cujas notificações serão listadas", required = true)
            @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(notificacaoService.buscarPorUsuario(usuarioId));
    }

    /**
     * Lista notificações não lidas de um usuário específico.
     * Requer autenticação.
     * 
     * @param usuarioId ID do usuário a ser buscado
     * @return Lista de notificações não lidas do usuário
     */
    @GetMapping("/usuario/{usuarioId}/nao-lidas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notificacao>> buscarNaoLidasPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(notificacaoService.buscarNaoLidasPorUsuario(usuarioId));
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Notificacao> criar(@Valid @RequestBody Notificacao notificacao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificacaoService.salvar(notificacao));
    }

    /**
     * Marca uma notificação como lida.
     * Requer autenticação.
     * 
     * @param id ID da notificação a ser marcada como lida
     * @return Notificação marcada como lida
     */
    @PatchMapping("/{id}/ler")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Notificacao> marcarComoLida(@PathVariable UUID id) {
        return notificacaoService.marcarComoLida(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação", "id", id));
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        if (!notificacaoService.existePorId(id)) {
            throw new ResourceNotFoundException("Notificação", "id", id);
        }
        notificacaoService.remover(id);
        return ResponseEntity.noContent().build();
    }
} 