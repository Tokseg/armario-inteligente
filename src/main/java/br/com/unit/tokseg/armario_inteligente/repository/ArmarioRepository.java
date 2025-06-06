package br.com.unit.tokseg.armario_inteligente.repository;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório responsável por operações de persistência relacionadas aos armários.
 * Estende JpaRepository para herdar operações básicas de CRUD.
 */
@Repository
public interface ArmarioRepository extends JpaRepository<Armario, UUID> {
    /**
     * Verifica se existe um armário com o número especificado.
     * 
     * @param numero Número do armário
     * @return true se existir um armário com o número, false caso contrário
     */
    boolean existsByNumero(String numero);

    /**
     * Busca um armário pelo número.
     * 
     * @param numero Número do armário
     * @return Optional contendo o armário encontrado, ou vazio se não existir
     */
    Optional<Armario> findByNumero(String numero);

    /**
     * Busca armários por status.
     * 
     * @param status Status dos armários
     * @return Lista de armários com o status especificado
     */
    List<Armario> findByStatus(ArmarioStatus status);

    /**
     * Busca armários por localização.
     * 
     * @param localizacao Localização dos armários
     * @return Lista de armários na localização especificada
     */
    List<Armario> findByLocalizacao(String localizacao);

    /**
     * Busca armários por status e localização.
     * 
     * @param status Status dos armários
     * @param localizacao Localização dos armários
     * @return Lista de armários que atendem aos critérios
     */
    List<Armario> findByStatusAndLocalizacao(ArmarioStatus status, String localizacao);

    /**
     * Conta quantos armários existem com um determinado status.
     * 
     * @param status Status a ser contado
     * @return Quantidade de armários com o status especificado
     */
    long countByStatus(ArmarioStatus status);

    /**
     * Busca armários disponíveis.
     * 
     * @return Lista de armários disponíveis
     */
    default List<Armario> findDisponiveis() {
        return findByStatus(ArmarioStatus.DISPONIVEL);
    }

    /**
     * Busca armários ocupados.
     * 
     * @return Lista de armários ocupados
     */
    default List<Armario> findOcupados() {
        return findByStatus(ArmarioStatus.OCUPADO);
    }

    /**
     * Busca armários em manutenção.
     * 
     * @return Lista de armários em manutenção
     */
    default List<Armario> findEmManutencao() {
        return findByStatus(ArmarioStatus.MANUTENCAO);
    }
}
