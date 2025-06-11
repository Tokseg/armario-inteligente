package br.com.unit.tokseg.armario_inteligente.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma encomenda no sistema.
 * Uma encomenda pode estar associada a um armário e a um usuário.
 * 
 * Relacionamentos:
 * - Uma encomenda pertence a um armário (opcional)
 * - Uma encomenda pertence a um usuário (opcional)
 */
@Entity
@Table(name = "Encomenda")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Encomenda {

    @Id
    @Column(name = "id_encomenda")
    private String idEncomenda;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String remetente;

    @Column(name = "dataRecebimento", nullable = false)
    private LocalDateTime dataRecebimento;

    @ManyToOne
    @JoinColumn(name = "armario_id", referencedColumnName = "id")
    private Armario armario;

    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "id")
    private Usuario usuario;

    // Getters e Setters
    public String getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(String idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public LocalDateTime getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDateTime dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public Armario getArmario() {
        return armario;
    }

    public void setArmario(Armario armario) {
        this.armario = armario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}