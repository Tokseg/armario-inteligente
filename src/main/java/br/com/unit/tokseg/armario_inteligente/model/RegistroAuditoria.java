package br.com.unit.tokseg.armario_inteligente.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entidade que representa um registro de auditoria no sistema.
 * Registra ações importantes realizadas no sistema para fins de auditoria.
 */
@Entity
@Table(name = "registro_auditoria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "A ação é obrigatória")
    @Column(nullable = false)
    private String acao;

    @Column
    private String detalhes;

    @NotNull(message = "A data e hora são obrigatórias")
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @PrePersist
    protected void onCreate() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }
}
