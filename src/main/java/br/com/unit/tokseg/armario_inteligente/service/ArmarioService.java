package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import br.com.unit.tokseg.armario_inteligente.repository.ArmarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócios relacionada aos armários.
 * Implementa as operações de CRUD e regras específicas do domínio.
 * 
 * Este serviço é responsável por:
 * - Validar operações antes de persistir no banco
 * - Implementar regras de negócio específicas
 * - Coordenar operações entre diferentes repositórios
 * - Fornecer uma interface limpa para os controllers
 */
@Service
public class ArmarioService {

    private static final Logger logger = LoggerFactory.getLogger(ArmarioService.class);
    private final ArmarioRepository armarioRepository;

    /**
     * Construtor explícito para inicializar o repositório de armário.
     * @param armarioRepository Repositório de armário a ser injetado
     */
    public ArmarioService(ArmarioRepository armarioRepository) {
        this.armarioRepository = armarioRepository;
    }

    /**
     * Verifica se já existe um armário com o número especificado.
     * 
     * @param numero Número do armário a ser verificado
     * @return true se o número já existe, false caso contrário
     * @throws IllegalArgumentException se o número for nulo ou vazio
     */
    public boolean existeNumero(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("Número do armário não pode ser nulo ou vazio");
        }
        logger.debug("Verificando existência do armário com número: {}", numero);
        return armarioRepository.existsByNumero(numero);
    }

    /**
     * Salva um novo armário no sistema.
     * 
     * @param armario Armário a ser salvo
     * @return Armário salvo com ID gerado
     * @throws IllegalArgumentException se o armário for nulo ou inválido
     */
    @Transactional
    public Armario salvar(Armario armario) {
        if (armario == null) {
            throw new IllegalArgumentException("Armário não pode ser nulo");
        }
        if (armario.getNumero() == null || armario.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("Número do armário não pode ser nulo ou vazio");
        }
        if (armario.getStatus() == null) {
            throw new IllegalArgumentException("Status do armário não pode ser nulo");
        }
        if (armario.getLocalizacao() == null || armario.getLocalizacao().trim().isEmpty()) {
            throw new IllegalArgumentException("Localização do armário não pode ser nula ou vazia");
        }

        logger.info("Salvando novo armário: {}", armario.getNumero());
        return armarioRepository.save(armario);
    }

    /**
     * Lista todos os armários cadastrados no sistema.
     * 
     * @return Lista de todos os armários
     */
    public List<Armario> listarTodos() {
        logger.debug("Listando todos os armários");
        return armarioRepository.findAll();
    }

    /**
     * Busca armários por status.
     * 
     * @param status Status dos armários a serem buscados
     * @return Lista de armários com o status especificado
     * @throws IllegalArgumentException se o status for nulo
     */
    public List<Armario> buscarPorStatus(ArmarioStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        logger.debug("Buscando armários com status: {}", status);
        return armarioRepository.findByStatus(status);
    }

    /**
     * Busca armários por localização.
     * 
     * @param localizacao Localização dos armários
     * @return Lista de armários na localização especificada
     * @throws IllegalArgumentException se a localização for nula ou vazia
     */
    public List<Armario> buscarPorLocalizacao(String localizacao) {
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new IllegalArgumentException("Localização não pode ser nula ou vazia");
        }
        logger.debug("Buscando armários na localização: {}", localizacao);
        return armarioRepository.findByLocalizacao(localizacao);
    }

    /**
     * Busca armários combinando status e localização.
     * 
     * @param status Status dos armários
     * @param localizacao Localização dos armários
     * @return Lista de armários que atendem aos dois critérios
     * @throws IllegalArgumentException se status ou localização forem inválidos
     */
    public List<Armario> buscarPorStatusELocalizacao(ArmarioStatus status, String localizacao) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new IllegalArgumentException("Localização não pode ser nula ou vazia");
        }
        logger.debug("Buscando armários com status {} e localização {}", status, localizacao);
        return armarioRepository.findByStatusAndLocalizacao(status, localizacao);
    }

    /**
     * Atualiza o status de um armário específico.
     * 
     * @param id ID do armário a ser atualizado
     * @param novoStatus Novo status do armário
     * @return Optional contendo o armário atualizado, ou vazio se não encontrado
     * @throws IllegalArgumentException se o novo status for nulo
     */
    @Transactional
    public Optional<Armario> atualizarStatus(Long id, ArmarioStatus novoStatus) {
        if (id == null) {
            throw new IllegalArgumentException("ID do armário não pode ser nulo");
        }
        if (novoStatus == null) {
            throw new IllegalArgumentException("Novo status não pode ser nulo");
        }

        logger.info("Atualizando status do armário {} para {}", id, novoStatus);
        return armarioRepository.findById(id)
                .map(armario -> {
                    armario.setStatus(novoStatus);
                    return armarioRepository.save(armario);
                });
    }

    /**
     * Busca um armário específico pelo ID.
     * 
     * @param id ID do armário a ser buscado
     * @return Optional contendo o armário encontrado, ou vazio se não existir
     * @throws IllegalArgumentException se o ID for nulo
     */
    public Optional<Armario> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do armário não pode ser nulo");
        }
        logger.debug("Buscando armário com ID: {}", id);
        return armarioRepository.findById(id);
    }

    /**
     * Conta quantos armários existem com um determinado status.
     * 
     * @param status Status a ser contado
     * @return Quantidade de armários com o status especificado
     * @throws IllegalArgumentException se o status for nulo
     */
    public long contarPorStatus(ArmarioStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        logger.debug("Contando armários com status: {}", status);
        return armarioRepository.countByStatus(status);
    }
} 