package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Usuario;
import br.com.unit.tokseg.armario_inteligente.model.TipoUsuarioEnum;
import br.com.unit.tokseg.armario_inteligente.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor que recebe as dependências necessárias via injeção de dependência.
     * 
     * @param usuarioRepository Repositório de usuários
     * @param passwordEncoder Codificador de senhas
     */
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Verifica se já existe um usuário com o email especificado.
     * 
     * @param email Email do usuário a ser verificado
     * @return true se o email já existe, false caso contrário
     * @throws IllegalArgumentException se o email for nulo ou vazio
     */
    public boolean existeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        logger.debug("Verificando existência do usuário com email: {}", email);
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Salva um novo usuário no sistema.
     * 
     * @param usuario Usuário a ser salvo
     * @return Usuário salvo com ID gerado
     * @throws IllegalArgumentException se o usuário for nulo ou inválido
     */
    @Transactional
    public Usuario salvar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }

        // Validações básicas
        if (!StringUtils.hasText(usuario.getNome())) {
            throw new IllegalArgumentException("Nome do usuário não pode ser nulo ou vazio");
        }
        if (!StringUtils.hasText(usuario.getEmail())) {
            throw new IllegalArgumentException("Email do usuário não pode ser nulo ou vazio");
        }
        if (!StringUtils.hasText(usuario.getSenha())) {
            throw new IllegalArgumentException("Senha do usuário não pode ser nula ou vazia");
        }
        if (!StringUtils.hasText(usuario.getTelefone())) {
            throw new IllegalArgumentException("Telefone do usuário não pode ser nulo ou vazio");
        }
        if (usuario.getTipo() == null) {
            throw new IllegalArgumentException("Tipo do usuário não pode ser nulo");
        }

        // Verifica se o email já existe
        if (existeEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com o email: " + usuario.getEmail());
        }

        // Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        logger.info("Salvando novo usuário: {}", usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    /**
     * Atualiza os dados de um usuário existente.
     * 
     * @param id ID do usuário a ser atualizado
     * @param usuarioAtualizado Dados atualizados do usuário
     * @return Usuário atualizado
     * @throws IllegalArgumentException se o ID ou usuário forem nulos
     * @throws EntityNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public Usuario atualizar(UUID id, Usuario usuarioAtualizado) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        if (usuarioAtualizado == null) {
            throw new IllegalArgumentException("Dados do usuário não podem ser nulos");
        }

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        // Verifica se o novo email já existe em outro usuário
        if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail()) 
            && existeEmail(usuarioAtualizado.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com o email: " + usuarioAtualizado.getEmail());
        }

        // Atualiza os campos permitidos
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setTelefone(usuarioAtualizado.getTelefone());
        usuarioExistente.setTipo(usuarioAtualizado.getTipo());

        // Se uma nova senha foi fornecida, atualiza a senha
        if (StringUtils.hasText(usuarioAtualizado.getSenha())) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        logger.info("Atualizando usuário com ID: {}", id);
        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Atualiza a senha de um usuário.
     * 
     * @param id ID do usuário
     * @param novaSenha Nova senha do usuário
     * @return Usuário atualizado
     * @throws IllegalArgumentException se o ID ou senha forem nulos/vazios
     * @throws EntityNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public Usuario atualizarSenha(UUID id, String novaSenha) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        if (!StringUtils.hasText(novaSenha)) {
            throw new IllegalArgumentException("Nova senha não pode ser nula ou vazia");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        logger.info("Atualizando senha do usuário: {}", usuario.getEmail());
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        return usuarioRepository.save(usuario);
    }

    /**
     * Atualiza o status de ativação de um usuário.
     * 
     * @param id ID do usuário
     * @param ativo Novo status de ativação
     * @return Usuário atualizado
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public Usuario atualizarStatus(UUID id, boolean ativo) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        logger.info("Atualizando status do usuário {} para {}", usuario.getEmail(), ativo ? "ativo" : "inativo");
        usuario.setAtivo(ativo);
        return usuarioRepository.save(usuario);
    }

    /**
     * Atualiza o tipo de um usuário.
     * 
     * @param id ID do usuário
     * @param novoTipo Novo tipo do usuário
     * @return Usuário atualizado
     * @throws IllegalArgumentException se o ID ou tipo forem nulos
     * @throws EntityNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public Usuario atualizarTipo(UUID id, TipoUsuarioEnum novoTipo) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        if (novoTipo == null) {
            throw new IllegalArgumentException("Novo tipo não pode ser nulo");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        logger.info("Atualizando tipo do usuário {} de {} para {}", usuario.getEmail(), usuario.getTipo(), novoTipo);
        usuario.setTipo(novoTipo);
        return usuarioRepository.save(usuario);
    }

    /**
     * Remove um usuário do sistema.
     * 
     * @param id ID do usuário a ser removido
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public void remover(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com ID: " + id);
        }

        logger.info("Removendo usuário com ID: {}", id);
        usuarioRepository.deleteById(id);
    }

    /**
     * Lista todos os usuários cadastrados no sistema.
     * 
     * @return Lista de todos os usuários
     */
    public List<Usuario> listarTodos() {
        logger.debug("Listando todos os usuários");
        return usuarioRepository.findAll();
    }

    /**
     * Busca um usuário específico pelo ID.
     * 
     * @param id ID do usuário a ser buscado
     * @return Optional contendo o usuário encontrado, ou vazio se não existir
     * @throws IllegalArgumentException se o ID for nulo
     */
    public Optional<Usuario> buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        logger.debug("Buscando usuário com ID: {}", id);
        return usuarioRepository.findById(id);
    }

    /**
     * Busca um usuário pelo email.
     * 
     * @param email Email do usuário
     * @return Optional contendo o usuário encontrado, ou vazio se não existir
     * @throws IllegalArgumentException se o email for nulo ou vazio
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        logger.debug("Buscando usuário com email: {}", email);
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Busca usuários por tipo.
     * 
     * @param tipo Tipo dos usuários a serem buscados
     * @return Lista de usuários do tipo especificado
     * @throws IllegalArgumentException se o tipo for nulo
     */
    public List<Usuario> buscarPorTipo(TipoUsuarioEnum tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo não pode ser nulo");
        }
        logger.debug("Buscando usuários do tipo: {}", tipo);
        return usuarioRepository.findByTipo(tipo);
    }

    /**
     * Verifica se um usuário está ativo.
     * 
     * @param id ID do usuário
     * @return true se o usuário estiver ativo, false caso contrário
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se o usuário não for encontrado
     */
    public boolean estaAtivo(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        return usuario.isEnabled();
    }
} 