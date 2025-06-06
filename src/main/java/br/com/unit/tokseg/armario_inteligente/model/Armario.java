/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package br.com.unit.tokseg.armario_inteligente.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Entidade que representa um armário inteligente no sistema.
 * Um armário pode estar disponível, ocupado ou em manutenção.
 * Cada armário tem um número único e uma localização específica.
 * 
 * Relacionamentos:
 * - Um armário pode ter uma encomenda atual (opcional)
 * - Um armário pode ter vários compartimentos (relacionamento não mapeado nesta classe)
 */
@Entity
@Table(name = "armario")
@Data
@NoArgsConstructor
public class Armario {
    /**
     * Identificador único do armário.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Número identificador do armário.
     * Deve ser único no sistema.
     */
    @NotBlank(message = "Número do armário é obrigatório")
    @Column(nullable = false, unique = true)
    private String numero;

    /**
     * Status atual do armário.
     * Pode ser DISPONIVEL, OCUPADO ou MANUTENCAO.
     */
    @NotNull(message = "Status do armário é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArmarioStatus status;

    /**
     * Localização física do armário.
     * Exemplo: "Bloco A, 1º andar"
     */
    @NotBlank(message = "Localização é obrigatória")
    @Column(nullable = false)
    private String localizacao;

    /**
     * Encomenda atual no armário, se houver.
     * Relacionamento opcional (nullable = true por padrão).
     */
    @ManyToOne
    @JoinColumn(name = "id_encomenda_atual", referencedColumnName = "id")
    private Encomenda encomendaAtual;

    /**
     * Observações sobre o armário.
     * Campo opcional para informações adicionais.
     */
    @Column(length = 500)
    private String observacoes;

    /**
     * Verifica se o armário está ocupado.
     * 
     * @return true se o armário estiver ocupado, false caso contrário
     */
    public boolean isOcupado() {
        return status == ArmarioStatus.OCUPADO;
    }

    /**
     * Verifica se o armário está em manutenção.
     * 
     * @return true se o armário estiver em manutenção, false caso contrário
     */
    public boolean isEmManutencao() {
        return status == ArmarioStatus.MANUTENCAO;
    }

    /**
     * Verifica se o armário está disponível.
     * 
     * @return true se o armário estiver disponível, false caso contrário
     */
    public boolean isDisponivel() {
        return status == ArmarioStatus.DISPONIVEL;
    }
}
