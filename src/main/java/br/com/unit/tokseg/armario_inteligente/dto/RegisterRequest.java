package br.com.unit.tokseg.armario_inteligente.dto;

import br.com.unit.tokseg.armario_inteligente.model.TipoUsuarioEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de registro de usuário.
 * Contém os dados necessários para criar um novo usuário no sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;
    private TipoUsuarioEnum tipo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TipoUsuarioEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuarioEnum tipo) {
        this.tipo = tipo;
    }
} 