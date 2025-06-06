package br.com.unit.tokseg.armario_inteligente.repository;

import br.com.unit.tokseg.armario_inteligente.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {
    List<Notificacao> findByUsuarioId(UUID usuarioId);
    List<Notificacao> findByUsuarioIdAndLidaFalse(UUID usuarioId);
} 