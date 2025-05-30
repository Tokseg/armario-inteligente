package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.dto.AuthenticationRequest;
import br.com.unit.tokseg.armario_inteligente.dto.AuthenticationResponse;
import br.com.unit.tokseg.armario_inteligente.dto.RegisterRequest;
import br.com.unit.tokseg.armario_inteligente.model.Usuario;
import br.com.unit.tokseg.armario_inteligente.model.TipoUsuarioEnum;
import br.com.unit.tokseg.armario_inteligente.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Serviço responsável por gerenciar a autenticação e registro de usuários.
 * Implementa as operações de registro de novos usuários e autenticação de usuários existentes.
 * 
 * Este serviço:
 * - Registra novos usuários no sistema
 * - Autentica usuários existentes
 * - Gera tokens JWT para usuários autenticados
 * - Gerencia as permissões e papéis dos usuários
 */
@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Construtor que recebe as dependências necessárias via injeção de dependência.
     * 
     * @param usuarioRepository Repositório de usuários
     * @param passwordEncoder Codificador de senhas
     * @param jwtService Serviço JWT
     * @param authenticationManager Gerenciador de autenticação
     */
    public AuthenticationService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra um novo usuário no sistema.
     * 
     * @param request Dados do usuário a ser registrado
     * @return Resposta contendo o token JWT gerado
     * @throws IllegalArgumentException se os dados do usuário forem inválidos
     */
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        logger.info("Iniciando registro de novo usuário: {}", request.getEmail());

        // Validação dos campos obrigatórios
        if (!StringUtils.hasText(request.getNome()) || 
            !StringUtils.hasText(request.getEmail()) || 
            !StringUtils.hasText(request.getSenha())) {
            logger.warn("Tentativa de registro com campos obrigatórios ausentes");
            throw new IllegalArgumentException("Nome, email e senha são obrigatórios");
        }

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Tentativa de registro com email já existente: {}", request.getEmail());
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTelefone(request.getTelefone());
        usuario.setTipo(request.getTipo() != null ? request.getTipo() : TipoUsuarioEnum.MORADOR);

        usuarioRepository.save(usuario);
        logger.info("Usuário registrado com sucesso: {}", usuario.getEmail());

        String jwtToken = jwtService.generateToken(usuario);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtToken);
        return response;
    }

    /**
     * Autentica um usuário existente.
     * 
     * @param request Dados de autenticação do usuário
     * @return Resposta contendo o token JWT gerado
     * @throws IllegalArgumentException se as credenciais forem inválidas
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        logger.info("Iniciando autenticação para usuário: {}", request.getEmail());

        // Validação dos campos obrigatórios
        if (!StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getSenha())) {
            logger.warn("Tentativa de autenticação com campos obrigatórios ausentes");
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );

            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            String jwtToken = jwtService.generateToken(usuario);
            logger.info("Usuário autenticado com sucesso: {}", usuario.getEmail());

            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(jwtToken);
            return response;

        } catch (Exception e) {
            logger.error("Falha na autenticação do usuário: {}", request.getEmail(), e);
            throw new IllegalArgumentException("Credenciais inválidas");
        }
    }
} 