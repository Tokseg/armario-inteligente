package br.com.unit.tokseg.armario_inteligente.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Compartimento")
public class Compartimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompartimento;

    @ManyToOne
    @JoinColumn(name = "idArmario", referencedColumnName = "idArmario")
    private Armario armario;

    private boolean ocupado;

    @ManyToOne
    @JoinColumn(name = "idEncomendaAtual", referencedColumnName = "idEncomenda")
    private Encomenda encomendaAtual;

    public Compartimento() {}

    public Compartimento(Armario armario, boolean ocupado, Encomenda encomendaAtual) {
        this.armario = armario;
        this.ocupado = ocupado;
        this.encomendaAtual = encomendaAtual;
    }

    public Long getIdCompartimento() { return idCompartimento; }
    public Armario getArmario() { return armario; }
    public boolean isOcupado() { return ocupado; }
    public Encomenda getEncomendaAtual() { return encomendaAtual; }
    public void setIdCompartimento(Long idCompartimento) { this.idCompartimento = idCompartimento; }
    public void setArmario(Armario armario) { this.armario = armario; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }
    public void setEncomendaAtual(Encomenda encomendaAtual) { this.encomendaAtual = encomendaAtual; }
} 