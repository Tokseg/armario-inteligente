package br.com.unit.tokseg.armario_inteligente.repository;

import br.com.unit.tokseg.armario_inteligente.model.Compartimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositório responsável por gerenciar o acesso aos dados dos compartimentos.
 */
@Repository
public interface CompartimentoRepository extends JpaRepository<Compartimento, UUID> {

    /**
     * Busca todos os compartimentos associados a um armário específico.
     *
     * @param armarioId ID do armário
     * @return Lista de compartimentos do armário
     */
    List<Compartimento> findByArmarioId(UUID armarioId);
} 