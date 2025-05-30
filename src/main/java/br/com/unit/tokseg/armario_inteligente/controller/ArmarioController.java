package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import br.com.unit.tokseg.armario_inteligente.service.ArmarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por gerenciar as operações relacionadas aos armários inteligentes.
 * Expõe endpoints REST para criar, listar, buscar e atualizar armários.
 * 
 * Endpoints disponíveis:
 * - POST /api/armarios: Cria um novo armário (ADMIN)
 * - GET /api/armarios: Lista todos os armários (AUTENTICADO)
 * - GET /api/armarios/{id}: Busca um armário específico (AUTENTICADO)
 * - GET /api/armarios/status/{status}: Busca armários por status (AUTENTICADO)
 * - GET /api/armarios/localizacao/{localizacao}: Busca armários por localização (AUTENTICADO)
 * - GET /api/armarios/filtro: Busca armários por status e localização (AUTENTICADO)
 * - PUT /api/armarios/{id}/status: Atualiza o status de um armário (ADMIN)
 * - GET /api/armarios/contar/{status}: Conta armários por status (AUTENTICADO)
 */
@RestController
@RequestMapping("/api/armarios")
public class ArmarioController {

    private final ArmarioService armarioService;

    /**
     * Construtor explícito para inicializar o serviço de armário.
     * @param armarioService Serviço de armário a ser injetado
     */
    public ArmarioController(ArmarioService armarioService) {
        this.armarioService = armarioService;
    }

    /**
     * Cria um novo armário no sistema.
     * Verifica se já existe um armário com o mesmo número antes de criar.
     * Requer permissão de ADMIN.
     * 
     * @param armario Dados do armário a ser criado
     * @return ResponseEntity com o armário criado ou erro 400 se o número já existir
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Armario> criar(@Valid @RequestBody Armario armario) {
        if (armarioService.existeNumero(armario.getNumero())) {
            return ResponseEntity.badRequest().build();
        }

        Armario novo = armarioService.salvar(armario);
        return ResponseEntity.ok(novo);
    }

    /**
     * Lista todos os armários cadastrados no sistema.
     * Requer autenticação.
     * 
     * @return Lista de todos os armários
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Armario>> listarTodos() {
        return ResponseEntity.ok(armarioService.listarTodos());
    }

    /**
     * Busca armários por status (DISPONIVEL, OCUPADO, MANUTENCAO).
     * Requer autenticação.
     * 
     * @param status Status dos armários a serem buscados
     * @return Lista de armários com o status especificado
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Armario>> buscarPorStatus(@PathVariable ArmarioStatus status) {
        return ResponseEntity.ok(armarioService.buscarPorStatus(status));
    }

    /**
     * Busca armários por localização.
     * Requer autenticação.
     * 
     * @param localizacao Localização dos armários a serem buscados
     * @return Lista de armários na localização especificada
     */
    @GetMapping("/localizacao/{localizacao}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Armario>> buscarPorLocalizacao(@PathVariable String localizacao) {
        return ResponseEntity.ok(armarioService.buscarPorLocalizacao(localizacao));
    }

    /**
     * Busca armários combinando status e localização.
     * Requer autenticação.
     * 
     * @param status Status dos armários
     * @param localizacao Localização dos armários
     * @return Lista de armários que atendem aos dois critérios
     */
    @GetMapping("/filtro")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Armario>> buscarPorStatusELocalizacao(
            @RequestParam ArmarioStatus status,
            @RequestParam String localizacao) {
        return ResponseEntity.ok(armarioService.buscarPorStatusELocalizacao(status, localizacao));
    }

    /**
     * Atualiza o status de um armário específico.
     * Requer permissão de ADMIN.
     * 
     * @param id ID do armário a ser atualizado
     * @param novoStatus Novo status do armário
     * @return Armário atualizado ou erro 404 se não encontrado
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Armario> atualizarStatus(
            @PathVariable Long id,
            @RequestParam ArmarioStatus novoStatus) {
        return armarioService.atualizarStatus(id, novoStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca um armário específico pelo ID.
     * Requer autenticação.
     * 
     * @param id ID do armário a ser buscado
     * @return Armário encontrado ou erro 404 se não existir
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Armario> buscarPorId(@PathVariable Long id) {
        return armarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Conta quantos armários existem com um determinado status.
     * Requer autenticação.
     * 
     * @param status Status a ser contado
     * @return Quantidade de armários com o status especificado
     */
    @GetMapping("/contar/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> contarPorStatus(@PathVariable ArmarioStatus status) {
        return ResponseEntity.ok(armarioService.contarPorStatus(status));
    }
}
