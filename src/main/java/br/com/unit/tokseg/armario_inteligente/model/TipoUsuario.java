package br.com.unit.tokseg.armario_inteligente.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um tipo de usuário no sistema.
 * Esta entidade está sendo mantida por compatibilidade com dados existentes,
 * mas o sistema agora usa o enum TipoUsuarioEnum para os tipos de usuário.
 * 
 * @deprecated Use TipoUsuarioEnum ao invés desta classe
 */
@Entity
@Table(name = "tipo_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @OneToMany(mappedBy = "tipo")
    @JsonIgnore
    private List<Usuario> usuarios;
}