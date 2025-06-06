package br.com.unit.tokseg.armario_inteligente.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.PrePersist;
import lombok.Setter;

/**
 * Entidade que representa uma encomenda no sistema.
 * Uma encomenda é associada a um usuário e pode estar em um compartimento de um armário.
 */
@Entity
@Table(name = "encomenda")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O código de rastreio é obrigatório")
    @Size(min = 6, max = 50, message = "O código de rastreio deve ter entre 6 e 50 caracteres")
    @Column(name = "codigo_rastreio", nullable = false, unique = true, length = 50)
    private String codigoRastreio;

    @NotBlank(message = "A descrição da encomenda é obrigatória")
    @Size(min = 3, max = 500, message = "A descrição deve ter entre 3 e 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descricao;

    @NotBlank(message = "O remetente é obrigatório")
    @Size(min = 3, max = 100, message = "O remetente deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String remetente;

    @Column(name = "data_recebimento", nullable = false)
    @NotNull(message = "A data de recebimento é obrigatória")
    private LocalDateTime dataRecebimento;

    @Column(name = "data_retirada")
    private LocalDateTime dataRetirada;

    @Column(name = "retirada_confirmada", nullable = false)
    private boolean retiradaConfirmada;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "id_armario", nullable = false)
    @NotNull(message = "O armário é obrigatório")
    private Armario armario;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @NotNull(message = "O usuário é obrigatório")
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {
        retiradaConfirmada = false;
    }
}