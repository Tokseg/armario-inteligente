package br.com.unit.tokseg.armario_inteligente.repository;

import br.com.unit.tokseg.armario_inteligente.model.Encomenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EncomendaRepository extends JpaRepository<Encomenda, UUID> {
    List<Encomenda> findByUsuarioId(UUID usuarioId);
    List<Encomenda> findByArmarioId(UUID armarioId);
    boolean existsByCodigoRastreio(String codigoRastreio);
} 