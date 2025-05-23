package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.Usuario;
import br.com.unit.tokseg.armario_inteligente.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por gerenciar as operações relacionadas aos usuários do sistema.
 * Expõe endpoints REST para criar, listar, buscar e deletar usuários.
 * 
 * Endpoints disponíveis:
 * - POST /api/usuarios: Cria um novo usuário
 * - GET /api/usuarios: Lista todos os usuários
 * - GET /api/usuarios/{id}: Busca um usuário específico
 * - DELETE /api/usuarios/{id}: Remove um usuário
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Construtor que recebe o serviço de usuários via injeção de dependência.
     * O Spring Boot gerencia automaticamente a criação e injeção do serviço.
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Cria um novo usuário no sistema.
     * Verifica se já existe um usuário com o mesmo email antes de criar.
     * 
     * @param usuario Dados do usuário a ser criado
     * @return ResponseEntity com o usuário criado ou erro 400 se o email já existir
     */
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        if (usuarioService.existeEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(usuarioService.salvar(usuario));
    }

    /**
     * Lista todos os usuários cadastrados no sistema.
     * 
     * @return Lista de todos os usuários
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * Busca um usuário específico pelo ID.
     * 
     * @param id ID do usuário a ser buscado
     * @return Usuário encontrado ou erro 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove um usuário do sistema.
     * 
     * @param id ID do usuário a ser removido
     * @return ResponseEntity sem conteúdo (204) se removido com sucesso, ou erro 404 se não encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return usuarioService.deletar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
