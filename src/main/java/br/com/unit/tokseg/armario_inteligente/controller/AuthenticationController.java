package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.dto.AuthenticationRequest;
import br.com.unit.tokseg.armario_inteligente.dto.AuthenticationResponse;
import br.com.unit.tokseg.armario_inteligente.dto.RegisterRequest;
import br.com.unit.tokseg.armario_inteligente.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

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
     * 
     * @param request Dados do usuário a ser registrado
     * @return Resposta contendo o token JWT gerado
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
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
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
} 