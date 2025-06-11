package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.dto.AuthenticationRequest;
import br.com.unit.tokseg.armario_inteligente.dto.AuthenticationResponse;
import br.com.unit.tokseg.armario_inteligente.dto.RegisterRequest;
import br.com.unit.tokseg.armario_inteligente.model.TipoUsuarioEnum;
import br.com.unit.tokseg.armario_inteligente.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável por gerenciar as operações de autenticação e registro de usuários.
 * Expõe endpoints REST para registro e autenticação de usuários.
 * 
 * Endpoints disponíveis:
 * - POST /api/v1/auth/register: Registra um novo usuário
 * - POST /api/v1/auth/authenticate: Autentica um usuário existente
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Construtor que recebe o serviço de autenticação via injeção de dependência.
     * 
     * @param authenticationService Serviço de autenticação a ser injetado
     */
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Registra um novo usuário no sistema.
     * O tipo de usuário (ADMIN, PORTEIRO, MORADOR) deve ser especificado na requisição.
     * Se não for especificado, o tipo padrão será MORADOR.
     * 
     * @param request Dados do usuário a ser registrado, incluindo o tipo (opcional)
     * @return Resposta contendo o token JWT gerado
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        // Se o tipo não for especificado, define como MORADOR por padrão
        if (request.getTipo() == null) {
            request.setTipo(TipoUsuarioEnum.MORADOR);
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Autentica um usuário existente.
     * 
     * @param request Dados de autenticação do usuário
     * @return Resposta contendo o token JWT gerado
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
} 