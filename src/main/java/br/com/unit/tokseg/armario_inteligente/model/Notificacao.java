package br.com.unit.tokseg.armario_inteligente.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notificacao")
public class Notificacao {
    @Id
    private String idNotificacao;

    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "id")
    private Usuario usuario;

    private String mensagem;
    private LocalDateTime dataEnvio;
    private boolean lida;

    public Notificacao(String idNotificacao, Usuario usuario, String mensagem, LocalDateTime dataEnvio) {
        this.idNotificacao = idNotificacao;
        this.usuario = usuario;
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
        this.lida = false;
    }

    public String getIdNotificacao() { return idNotificacao; }
    public Usuario getUsuario() { return usuario; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public boolean isLida() { return lida; }
    public void setIdNotificacao(String idNotificacao) { this.idNotificacao = idNotificacao; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
    public void setLida(boolean lida) { this.lida = lida; }
} 