package br.com.unit.tokseg.armario_inteligente.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "Encomenda")
public class Encomenda {

    @Id
    @Column(name = "idEncomenda")
    private String idEncomenda;

    private String descricao;
    private String remetente;

    @Column(name = "dataRecebimento")
    private LocalDateTime dataRecebimento;

    @ManyToOne
    @JoinColumn(name = "idArmario", referencedColumnName = "idArmario")
    //Só esperando criar o model de armario
    private Armario idArmario;

    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    //Só esperando criar o model de usuário
    private Usuario idUsuario;

    public Encomenda() {
        // construtor padrão exigido pelo JPA
    }

    public Encomenda(String idEncomenda, String descricao, String remetente, LocalDateTime dataRecebimento, Armario armario, Usuario usuario) {
        this.idEncomenda = idEncomenda;
        this.descricao = descricao;
        this.remetente = remetente;
        this.dataRecebimento = dataRecebimento;
        this.idArmario = armario;
        this.idUsuario = usuario;
    }

    public String getIdEncomenda() {
        return idEncomenda;
    }

    //Só esperando criar o model de usuário
    public Usuario getIdUsuario() {
        return idUsuario;
    }

    //Só esperando criar o model de armario
    public Armario getIdArmario() {
        return idArmario;
    }
}