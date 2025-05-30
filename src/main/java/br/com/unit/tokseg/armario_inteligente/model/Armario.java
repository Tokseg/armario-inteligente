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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(nullable = false, unique = true)
    private String numero;

    /**
     * Status atual do armário.
     * Pode ser DISPONIVEL, OCUPADO ou MANUTENCAO.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArmarioStatus status;

    /**
     * Localização física do armário.
     * Exemplo: "Bloco A, 1º andar"
     */
    @Column(nullable = false)
    private String localizacao;

    /**
     * Encomenda atual no armário, se houver.
     * Relacionamento opcional (nullable = true por padrão).
     */
    @ManyToOne
    @JoinColumn(name = "id_encomenda_atual", referencedColumnName = "id_encomenda")
    private Encomenda encomendaAtual;

    public boolean isOcupado() {
        return status == ArmarioStatus.OCUPADO;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public String getNumero() { return numero; }
    public ArmarioStatus getStatus() { return status; }
    public String getLocalizacao() { return localizacao; }
    public Encomenda getEncomendaAtual() { return encomendaAtual; }

    public void setId(UUID id) { this.id = id; }
    public void setNumero(String numero) { this.numero = numero; }
    public void setStatus(ArmarioStatus status) { this.status = status; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public void setEncomendaAtual(Encomenda encomendaAtual) { this.encomendaAtual = encomendaAtual; }
}
