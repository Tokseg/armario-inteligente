package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import br.com.unit.tokseg.armario_inteligente.service.ArmarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsável por gerenciar as operações relacionadas aos armários inteligentes.
 * Expõe endpoints REST para criar, listar, buscar e atualizar armários.
 * 
 * Endpoints disponíveis:
 * - POST /api/armarios: Cria um novo armário
 * - GET /api/armarios: Lista todos os armários
 * - GET /api/armarios/{id}: Busca um armário específico
 * - GET /api/armarios/status/{status}: Busca armários por status
 * - GET /api/armarios/localizacao/{localizacao}: Busca armários por localização
 * - GET /api/armarios/filtro: Busca armários por status e localização
 * - PUT /api/armarios/{id}/status: Atualiza o status de um armário
 * - GET /api/armarios/contar/{status}: Conta armários por status
 */
@RestController
@RequestMapping("/api/armarios")
public class ArmarioController {

    private final ArmarioService armarioService;

    /**
     * Construtor que recebe o serviço de armários via injeção de dependência.
     * O Spring Boot gerencia automaticamente a criação e injeção do serviço.
     */
    public ArmarioController(ArmarioService armarioService) {
        this.armarioService = armarioService;
    }

    /**
     * Cria um novo armário no sistema.
     * Verifica se já existe um armário com o mesmo número antes de criar.
     * 
     * @param armario Dados do armário a ser criado
     * @return ResponseEntity com o armário criado ou erro 400 se o número já existir
     */
    @PostMapping
    public ResponseEntity<Armario> criar(@RequestBody Armario armario) {
        if (armarioService.existeNumero(armario.getNumero())) {
            return ResponseEntity.badRequest().build();
        }

        Armario novo = armarioService.salvar(armario, "sistema");
        return ResponseEntity.ok(novo);
    }

    /**
     * Lista todos os armários cadastrados no sistema.
     * 
     * @return Lista de todos os armários
     */
    @GetMapping
    public ResponseEntity<List<Armario>> listarTodos() {
        return ResponseEntity.ok(armarioService.listarTodos());
    }

    /**
     * Busca armários por status (DISPONIVEL, OCUPADO, MANUTENCAO).
     * 
     * @param status Status dos armários a serem buscados
     * @return Lista de armários com o status especificado
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Armario>> buscarPorStatus(@PathVariable ArmarioStatus status) {
        return ResponseEntity.ok(armarioService.buscarPorStatus(status));
    }

    /**
     * Busca armários por localização.
     * Útil para encontrar armários em uma área específica.
     * 
     * @param localizacao Localização dos armários a serem buscados
     * @return Lista de armários na localização especificada
     */
    @GetMapping("/localizacao/{localizacao}")
    public ResponseEntity<List<Armario>> buscarPorLocalizacao(@PathVariable String localizacao) {
        return ResponseEntity.ok(armarioService.buscarPorLocalizacao(localizacao));
    }

    /**
     * Busca armários combinando status e localização.
     * Permite filtrar armários por ambos os critérios simultaneamente.
     * 
     * @param status Status dos armários
     * @param localizacao Localização dos armários
     * @return Lista de armários que atendem aos dois critérios
     */
    @GetMapping("/filtro")
    public ResponseEntity<List<Armario>> buscarPorStatusELocalizacao(
            @RequestParam ArmarioStatus status,
            @RequestParam String localizacao) {
        return ResponseEntity.ok(armarioService.buscarPorStatusELocalizacao(status, localizacao));
    }

    /**
     * Atualiza o status de um armário específico.
     * Útil para marcar um armário como disponível, ocupado ou em manutenção.
     * 
     * @param id ID do armário a ser atualizado
     * @param status Novo status do armário
     * @return Armário atualizado ou erro 404 se não encontrado
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Armario> atualizarStatus(
            @PathVariable Long id,
            @RequestParam ArmarioStatus status) {
        Optional<Armario> atualizado = armarioService.atualizarStatus(id, status);
        return atualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Busca um armário específico pelo ID.
     * 
     * @param id ID do armário a ser buscado
     * @return Armário encontrado ou erro 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Armario> buscarPorId(@PathVariable Long id) {
        return armarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Conta quantos armários existem com um determinado status.
     * Útil para estatísticas e monitoramento do sistema.
     * 
     * @param status Status a ser contado
     * @return Quantidade de armários com o status especificado
     */
    @GetMapping("/contar/{status}")
    public ResponseEntity<Long> contarPorStatus(@PathVariable ArmarioStatus status) {
        return ResponseEntity.ok(armarioService.contarPorStatus(status));
    }
}
