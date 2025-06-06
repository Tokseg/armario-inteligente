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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Setter;

/**
 * Entidade que representa uma notificação no sistema.
 * Uma notificação pode ser enviada para um usuário sobre eventos importantes,
 * como chegada de encomendas, alertas de segurança, etc.
 * 
 * Relacionamentos:
 * - Uma notificação pertence a um usuário (obrigatório)
 */
@Entity
@Table(name = "notificacao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O título da notificação é obrigatório")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "A mensagem da notificação é obrigatória")
    @Column(nullable = false, length = 500)
    private String mensagem;

    @NotNull(message = "O tipo da notificação é obrigatório")
    @Column(name = "tipo_notificacao", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TipoNotificacao tipoNotificacao;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    @Column(nullable = false)
    private boolean lida;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @NotNull(message = "O usuário da notificação é obrigatório")
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        lida = false;
    }

    /**
     * Enum que define os tipos possíveis de notificação no sistema.
     */
    public enum TipoNotificacao {
        ENCOMENDA,
        SEGURANCA,
        SISTEMA,
        MANUTENCAO
    }
} 