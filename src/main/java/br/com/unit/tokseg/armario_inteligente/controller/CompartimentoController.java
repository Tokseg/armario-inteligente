package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.exception.ResourceNotFoundException;
import br.com.unit.tokseg.armario_inteligente.model.Compartimento;
import br.com.unit.tokseg.armario_inteligente.service.CompartimentoService;
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
 * Controller responsável por gerenciar as operações relacionadas aos compartimentos.
 * Expõe endpoints REST para criar, listar, buscar, atualizar e remover compartimentos.
 */
@RestController
@RequestMapping("/api/compartimentos")
@Tag(name = "Compartimentos", description = "API para gerenciamento de compartimentos")
@SecurityRequirement(name = "bearerAuth")
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

    @Operation(summary = "Lista todos os compartimentos", description = "Retorna uma lista de todos os compartimentos cadastrados no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de compartimentos retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Compartimento.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Compartimento>> listarTodos() {
        return ResponseEntity.ok(compartimentoService.listarTodos());
    }

    @Operation(summary = "Busca um compartimento por ID", description = "Retorna um compartimento específico baseado no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compartimento encontrado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Compartimento.class))),
        @ApiResponse(responseCode = "404", description = "Compartimento não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Compartimento> buscarPorId(
            @Parameter(description = "ID do compartimento a ser buscado", required = true)
            @PathVariable UUID id) {
        return compartimentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Compartimento", "id", id));
    }

    @Operation(summary = "Lista compartimentos por armário", description = "Retorna uma lista de compartimentos de um armário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de compartimentos retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Compartimento.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/armario/{armarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Compartimento>> buscarPorArmario(
            @Parameter(description = "ID do armário cujos compartimentos serão listados", required = true)
            @PathVariable UUID armarioId) {
        return ResponseEntity.ok(compartimentoService.buscarPorArmario(armarioId));
    }

    @Operation(summary = "Cria um novo compartimento", description = "Cria um novo compartimento no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compartimento criado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Compartimento.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Compartimento> criar(
            @Parameter(description = "Dados do compartimento a ser criado", required = true)
            @Valid @RequestBody Compartimento compartimento) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(compartimentoService.salvar(compartimento));
    }

    @Operation(summary = "Atualiza um compartimento", description = "Atualiza os dados de um compartimento existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compartimento atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Compartimento.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Compartimento não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Compartimento> atualizar(
            @Parameter(description = "ID do compartimento a ser atualizado", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Dados do compartimento atualizado", required = true)
            @Valid @RequestBody Compartimento compartimento) {
        if (!compartimentoService.existePorId(id)) {
            throw new ResourceNotFoundException("Compartimento", "id", id);
        }
        compartimento.setId(id);
        return ResponseEntity.ok(compartimentoService.salvar(compartimento));
    }

    @Operation(summary = "Remove um compartimento", description = "Remove um compartimento do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Compartimento removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Compartimento não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do compartimento a ser removido", required = true)
            @PathVariable UUID id) {
        if (!compartimentoService.existePorId(id)) {
            throw new ResourceNotFoundException("Compartimento", "id", id);
        }
        compartimentoService.remover(id);
        return ResponseEntity.noContent().build();
    }
} 