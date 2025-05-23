package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Usuario;
import br.com.unit.tokseg.armario_inteligente.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócios relacionada aos usuários.
 * Implementa as operações de CRUD e regras específicas do domínio.
 * 
 * Este serviço é responsável por:
 * - Validar operações antes de persistir no banco
 * - Implementar regras de negócio específicas
 * - Coordenar operações entre diferentes repositórios
 * - Fornecer uma interface limpa para os controllers
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor que recebe o repositório de usuários via injeção de dependência.
     * O Spring Boot gerencia automaticamente a criação e injeção do repositório.
     */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Verifica se já existe um usuário com o email especificado.
     * Usado para evitar duplicidade de emails no sistema.
     * 
     * @param email Email do usuário a ser verificado
     * @return true se o email já existe, false caso contrário
     */
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Salva um novo usuário no sistema.
     * O usuário deve ter um email único e válido.
     * 
     * @param usuario Usuário a ser salvo
     * @return Usuário salvo com ID gerado
     */
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Lista todos os usuários cadastrados no sistema.
     * 
     * @return Lista de todos os usuários
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca um usuário específico pelo ID.
     * 
     * @param id ID do usuário a ser buscado
     * @return Optional contendo o usuário encontrado, ou vazio se não existir
     */
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Remove um usuário do sistema.
     * Se o usuário não for encontrado, retorna false.
     * 
     * @param id ID do usuário a ser removido
     * @return true se o usuário foi removido com sucesso, false se não encontrado
     */
    public boolean deletar(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 