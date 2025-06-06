package br.com.unit.tokseg.armario_inteligente.repository;

import br.com.unit.tokseg.armario_inteligente.model.Usuario;
import br.com.unit.tokseg.armario_inteligente.model.TipoUsuarioEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório responsável por operações de persistência relacionadas aos usuários.
 * Estende JpaRepository para herdar operações básicas de CRUD.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    /**
     * Busca um usuário pelo email.
     * 
     * @param email Email do usuário
     * @return Optional contendo o usuário encontrado, ou vazio se não existir
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se existe um usuário com o email especificado.
     * 
     * @param email Email do usuário
     * @return true se existir um usuário com o email, false caso contrário
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuários por tipo.
     * 
     * @param tipo Tipo dos usuários
     * @return Lista de usuários do tipo especificado
     */
    List<Usuario> findByTipo(TipoUsuarioEnum tipo);

    /**
     * Busca usuários ativos.
     * 
     * @return Lista de usuários ativos
     */
    List<Usuario> findByAtivoTrue();

    /**
     * Busca usuários inativos.
     * 
     * @return Lista de usuários inativos
     */
    List<Usuario> findByAtivoFalse();

    /**
     * Busca usuários por tipo e status de ativação.
     * 
     * @param tipo Tipo dos usuários
     * @param ativo Status de ativação
     * @return Lista de usuários que atendem aos critérios
     */
    List<Usuario> findByTipoAndAtivo(TipoUsuarioEnum tipo, boolean ativo);
}
