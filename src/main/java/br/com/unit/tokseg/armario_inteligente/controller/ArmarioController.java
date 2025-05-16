package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import br.com.unit.tokseg.armario_inteligente.service.ArmarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/armarios")
public class ArmarioController {

    private final ArmarioService armarioService;

    @Autowired
    public ArmarioController(ArmarioService armarioService) {
        this.armarioService = armarioService;
    }

    // POST: Criar um novo armário
    @PostMapping
    public ResponseEntity<Armario> criar(@RequestBody Armario armario) {
        if (armarioService.existeNumero(armario.getNumero())) {
            return ResponseEntity.badRequest().build();
        }

        // Email fixo usado apenas para registrar auditoria nos testes manuais
        Armario novo = armarioService.salvar(armario, "teste@admin.com");

        return ResponseEntity.ok(novo);
    }

    // GET: Listar todos os armários
    @GetMapping
    public List<Armario> listarTodos() {
        return armarioService.listarTodos();
    }

    // GET: Buscar por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Armario>> buscarPorStatus(@PathVariable ArmarioStatus status) {
        return ResponseEntity.ok(armarioService.buscarPorStatus(status));
    }

    // GET: Buscar por localização
    @GetMapping("/localizacao/{localizacao}")
    public ResponseEntity<List<Armario>> buscarPorLocalizacao(@PathVariable String localizacao) {
        return ResponseEntity.ok(armarioService.buscarPorLocalizacao(localizacao));
    }

    // GET: Buscar por status e localização
    @GetMapping("/filtro")
    public ResponseEntity<List<Armario>> buscarPorStatusELocalizacao(
            @RequestParam ArmarioStatus status,
            @RequestParam String localizacao) {
        return ResponseEntity.ok(armarioService.buscarPorStatusELocalizacao(status, localizacao));
    }

    // PUT: Atualizar status do armário
    @PutMapping("/{id}/status")
    public ResponseEntity<Armario> atualizarStatus(@PathVariable Long id, @RequestParam ArmarioStatus status) {
        Optional<Armario> atualizado = armarioService.atualizarStatus(id, status);
        return atualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Armario> buscarPorId(@PathVariable Long id) {
        return armarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET: Contar armários por status
    @GetMapping("/contar/{status}")
    public long contarPorStatus(@PathVariable ArmarioStatus status) {
        return armarioService.contarPorStatus(status);
    }
}
