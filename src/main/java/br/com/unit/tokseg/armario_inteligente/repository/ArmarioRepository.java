package br.com.unit.tokseg.armario_inteligente.repository;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArmarioRepository extends JpaRepository<Armario, Long> {
    boolean existsByNumero(String numero);
    List<Armario> findByStatus(ArmarioStatus status);
    List<Armario> findByLocalizacao(String localizacao);
    List<Armario> findByStatusAndLocalizacao(ArmarioStatus status, String localizacao);
    long countByStatus(ArmarioStatus status);
}
