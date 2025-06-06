package br.com.unit.tokseg.armario_inteligente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de autenticação.
 * Contém as credenciais necessárias para autenticar um usuário no sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
} 