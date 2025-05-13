package br.com.unit.tokseg.armario_inteligente.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Column(nullable = false)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "tipo_usuario_id", nullable = false)
    private TipoUsuario tipoUsuario;

    public Usuario(){}
    public Usuario(String nome, String email, String senha, String telefone, TipoUsuario tipoUsuario){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome(){
        return nome;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setSenha(String senha){
        this.senha = senha;
    }
    public String getSenha(){
        return senha;
    }
    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
    public String getTelefone(){
        return telefone;
    }
    public void setTipoUsuario(TipoUsuario tipoUsuario){
        this.tipoUsuario = tipoUsuario;
    }
    public TipoUsuario getTipoUsuario(){
        return tipoUsuario;
    }
}
