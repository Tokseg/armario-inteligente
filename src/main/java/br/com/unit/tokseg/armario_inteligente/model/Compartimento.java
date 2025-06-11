package br.com.unit.tokseg.armario_inteligente.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "Compartimento")
public class Compartimento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idCompartimento;

    @ManyToOne
    @JoinColumn(name = "armario_id", referencedColumnName = "id")
    private Armario armario;

    private boolean ocupado;

    @ManyToOne
    @JoinColumn(name = "id_encomenda_atual", referencedColumnName = "id_encomenda")
    private Encomenda encomendaAtual;

    public Compartimento() {}

    public Compartimento(Armario armario, boolean ocupado, Encomenda encomendaAtual) {
        this.armario = armario;
        this.ocupado = ocupado;
        this.encomendaAtual = encomendaAtual;
    }

    public UUID getIdCompartimento() {
        return idCompartimento;
    }

    public Armario getArmario() {
        return armario;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public Encomenda getEncomendaAtual() {
        return encomendaAtual;
    }

    public void setIdCompartimento(UUID idCompartimento) {
        this.idCompartimento = idCompartimento;
    }

    public void setArmario(Armario armario) {
        this.armario = armario;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public void setEncomendaAtual(Encomenda encomendaAtual) {
        this.encomendaAtual = encomendaAtual;
    }
}
