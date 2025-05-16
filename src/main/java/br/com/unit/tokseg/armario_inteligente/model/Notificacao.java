package br.com.unit.tokseg.armario_inteligente.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Notificacao")
public class Notificacao {
    @Id
    private String idNotificacao;

    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    private String mensagem;
    private LocalDateTime dataEnvio;

    public Notificacao() {}

    public Notificacao(String idNotificacao, Usuario usuario, String mensagem, LocalDateTime dataEnvio) {
        this.idNotificacao = idNotificacao;
        this.usuario = usuario;
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
    }

    public String getIdNotificacao() { return idNotificacao; }
    public Usuario getUsuario() { return usuario; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setIdNotificacao(String idNotificacao) { this.idNotificacao = idNotificacao; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
} 