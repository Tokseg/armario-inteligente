package br.com.unit.tokseg.armario_inteligente.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro_auditoria")
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRegistro;

    @Column(nullable = false)
    private String acao;

    @Column
    private String detalhes;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public RegistroAuditoria() {
        // construtor padrão exigido pelo JPA
    }

    public RegistroAuditoria(int idRegistro, String acao, String detalhes, LocalDateTime dataHora){

        this.idRegistro = idRegistro;
        this.acao = acao;
        this.detalhes = detalhes;
        this.dataHora = dataHora;
    }

    public int getIdRegistro(){

        return idRegistro;
    }

    public String getAcao(){

        return acao;

    }

    public String getDetalhes(){

        return detalhes;
    }

    public LocalDateTime getDataHora(){

        return dataHora;
    }
    
}
