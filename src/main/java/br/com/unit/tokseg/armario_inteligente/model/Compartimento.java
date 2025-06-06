package br.com.unit.tokseg.armario_inteligente.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um compartimento de um armário.
 */
@Entity
@Table(name = "compartimento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compartimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O número do compartimento é obrigatório")
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull(message = "O tamanho do compartimento é obrigatório")
    @Positive(message = "O tamanho do compartimento deve ser positivo")
    @Column(name = "tamanho", nullable = false)
    private Double tamanho;

    @NotNull(message = "O status do compartimento é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusCompartimento status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_armario", nullable = false)
    @NotNull(message = "O armário é obrigatório")
    private Armario armario;

    @OneToOne(mappedBy = "compartimento", fetch = FetchType.LAZY)
    private Encomenda encomendaAtual;

    /**
     * Enum que representa os possíveis status de um compartimento.
     */
    public enum StatusCompartimento {
        DISPONIVEL,
        OCUPADO,
        MANUTENCAO
    }

    public boolean isOcupado() {
        return status == StatusCompartimento.OCUPADO;
    }

    public void setOcupado(boolean ocupado) {
        this.status = ocupado ? StatusCompartimento.OCUPADO : StatusCompartimento.DISPONIVEL;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Armario getArmario() {
        return armario;
    }

    public void setArmario(Armario armario) {
        this.armario = armario;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setTamanho(Double tamanho) {
        this.tamanho = tamanho;
    }

    public void setStatus(StatusCompartimento status) {
        this.status = status;
    }

    public StatusCompartimento getStatus() {
        return this.status;
    }
} 