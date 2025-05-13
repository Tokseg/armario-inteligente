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

@Entity
@Table(name = "tipo_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuario {

       @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

       @Column(nullable = false, unique = true)
        private String nome;

       @Column(length = 500)
        private String descricao;

       @OneToMany(mappedBy = "tipoUsuario")
       @JsonIgnore
        private List<Usuario> usuarios;

}