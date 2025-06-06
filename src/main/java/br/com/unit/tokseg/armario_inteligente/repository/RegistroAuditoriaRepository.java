package br.com.unit.tokseg.armario_inteligente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.unit.tokseg.armario_inteligente.model.RegistroAuditoria;
import java.util.UUID;

@Repository
public interface RegistroAuditoriaRepository extends JpaRepository<RegistroAuditoria, UUID> {
} 