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
@Table(name = "Armario")
public class Armario {
    /**
     * Identificador único do armário.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArmario;

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
    @Column(nullable = false)
    private ArmarioStatus status;

    /**
     * Localização física do armário.
     * Exemplo: "Bloco A, 1º andar"
     */
    @Column(nullable = false)
    private String localizacao;

    /**
     * Indica se o armário está ocupado.
     * Este campo é redundante com o status, mas mantido por compatibilidade.
     */
    private boolean ocupado;

    /**
     * Encomenda atual no armário, se houver.
     * Relacionamento opcional (nullable = true por padrão).
     */
    @ManyToOne
    @JoinColumn(name = "idEncomendaAtual", referencedColumnName = "idEncomenda")
    private Encomenda encomendaAtual;

    /**
     * Construtor padrão exigido pelo JPA.
     */
    public Armario() {}

    /**
     * Construtor completo para criar um armário com todos os dados.
     * 
     * @param numero Número identificador do armário
     * @param status Status inicial do armário
     * @param localizacao Localização do armário
     * @param ocupado Se o armário está ocupado
     * @param encomendaAtual Encomenda atual, se houver
     */
    public Armario(String numero, ArmarioStatus status, String localizacao, boolean ocupado, Encomenda encomendaAtual) {
        this.numero = numero;
        this.status = status;
        this.localizacao = localizacao;
        this.ocupado = ocupado;
        this.encomendaAtual = encomendaAtual;
    }

    // Getters e Setters
    public Long getIdArmario() { return idArmario; }
    public String getNumero() { return numero; }
    public ArmarioStatus getStatus() { return status; }
    public String getLocalizacao() { return localizacao; }
    public boolean isOcupado() { return ocupado; }
    public Encomenda getEncomendaAtual() { return encomendaAtual; }

    public void setIdArmario(Long idArmario) { this.idArmario = idArmario; }
    public void setNumero(String numero) { this.numero = numero; }
    public void setStatus(ArmarioStatus status) { this.status = status; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }
    public void setEncomendaAtual(Encomenda encomendaAtual) { this.encomendaAtual = encomendaAtual; }
}
