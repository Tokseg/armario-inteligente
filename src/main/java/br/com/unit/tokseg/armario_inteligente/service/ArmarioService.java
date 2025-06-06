package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import br.com.unit.tokseg.armario_inteligente.repository.ArmarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        if (!StringUtils.hasText(numero)) {
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
        
        if (!StringUtils.hasText(armario.getNumero())) {
            throw new IllegalArgumentException("Número do armário não pode ser nulo ou vazio");
        }
        
        if (armario.getStatus() == null) {
            throw new IllegalArgumentException("Status do armário não pode ser nulo");
        }
        
        if (!StringUtils.hasText(armario.getLocalizacao())) {
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
     * Busca um armário específico pelo ID.
     * 
     * @param id ID do armário a ser buscado
     * @return Optional contendo o armário encontrado, ou vazio se não existir
     * @throws IllegalArgumentException se o ID for nulo
     */
    public Optional<Armario> buscarPorId(UUID id) {
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

    /**
     * Verifica se um armário pode ser atualizado para um novo status.
     * 
     * @param armario Armário a ser verificado
     * @param novoStatus Novo status desejado
     * @return true se a atualização for permitida, false caso contrário
     */
    private boolean podeAtualizarStatus(Armario armario, ArmarioStatus novoStatus) {
        if (armario == null || novoStatus == null) {
            return false;
        }

        // Se o armário estiver em manutenção, só pode ser atualizado para disponível
        if (armario.isEmManutencao() && novoStatus != ArmarioStatus.DISPONIVEL) {
            logger.warn("Armário em manutenção só pode ser atualizado para disponível");
            return false;
        }

        // Se o armário estiver ocupado, só pode ser atualizado para disponível ou manutenção
        if (armario.isOcupado() && novoStatus == ArmarioStatus.OCUPADO) {
            logger.warn("Armário já está ocupado");
            return false;
        }

        // Se o armário estiver disponível, pode ser atualizado para qualquer status
        return true;
    }

    /**
     * Atualiza o status de um armário com validações adicionais.
     * 
     * @param id ID do armário a ser atualizado
     * @param novoStatus Novo status do armário
     * @return Armário atualizado
     * @throws IllegalArgumentException se o ID ou status forem nulos, ou se a atualização não for permitida
     * @throws EntityNotFoundException se o armário não for encontrado
     */
    @Transactional
    public Armario atualizarStatus(UUID id, ArmarioStatus novoStatus) {
        if (id == null) {
            throw new IllegalArgumentException("ID do armário não pode ser nulo");
        }
        if (novoStatus == null) {
            throw new IllegalArgumentException("Novo status não pode ser nulo");
        }

        Armario armario = armarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Armário não encontrado com ID: " + id));

        if (!podeAtualizarStatus(armario, novoStatus)) {
            throw new IllegalArgumentException("Não é possível atualizar o status do armário para " + novoStatus);
        }

        logger.info("Atualizando status do armário {} de {} para {}", id, armario.getStatus(), novoStatus);
        armario.setStatus(novoStatus);
        return armarioRepository.save(armario);
    }

    /**
     * Atualiza a localização de um armário.
     * 
     * @param id ID do armário a ser atualizado
     * @param novaLocalizacao Nova localização do armário
     * @return Armário atualizado
     * @throws IllegalArgumentException se o ID ou localização forem nulos/vazios
     * @throws EntityNotFoundException se o armário não for encontrado
     */
    @Transactional
    public Armario atualizarLocalizacao(UUID id, String novaLocalizacao) {
        if (id == null) {
            throw new IllegalArgumentException("ID do armário não pode ser nulo");
        }
        if (!StringUtils.hasText(novaLocalizacao)) {
            throw new IllegalArgumentException("Nova localização não pode ser nula ou vazia");
        }

        Armario armario = armarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Armário não encontrado com ID: " + id));

        logger.info("Atualizando localização do armário {} de {} para {}", id, armario.getLocalizacao(), novaLocalizacao);
        armario.setLocalizacao(novaLocalizacao);
        return armarioRepository.save(armario);
    }

    /**
     * Atualiza as observações de um armário.
     * 
     * @param id ID do armário a ser atualizado
     * @param observacoes Novas observações do armário
     * @return Armário atualizado
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se o armário não for encontrado
     */
    @Transactional
    public Armario atualizarObservacoes(UUID id, String observacoes) {
        if (id == null) {
            throw new IllegalArgumentException("ID do armário não pode ser nulo");
        }

        Armario armario = armarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Armário não encontrado com ID: " + id));

        logger.info("Atualizando observações do armário {}", id);
        armario.setObservacoes(observacoes);
        return armarioRepository.save(armario);
    }

    /**
     * Atualiza os dados de um armário existente.
     * 
     * @param id ID do armário a ser atualizado
     * @param armarioAtualizado Dados atualizados do armário
     * @return Armário atualizado
     * @throws IllegalArgumentException se o ID ou armário forem nulos
     * @throws EntityNotFoundException se o armário não for encontrado
     */
    @Transactional
    public Armario atualizar(UUID id, Armario armarioAtualizado) {
        if (id == null) {
            throw new IllegalArgumentException("ID do armário não pode ser nulo");
        }
        if (armarioAtualizado == null) {
            throw new IllegalArgumentException("Dados do armário não podem ser nulos");
        }

        Armario armarioExistente = armarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Armário não encontrado com ID: " + id));

        // Verifica se o novo número já existe em outro armário
        if (!armarioExistente.getNumero().equals(armarioAtualizado.getNumero()) 
            && existeNumero(armarioAtualizado.getNumero())) {
            throw new IllegalArgumentException("Já existe um armário com o número: " + armarioAtualizado.getNumero());
        }

        // Verifica se a atualização de status é permitida
        if (!armarioExistente.getStatus().equals(armarioAtualizado.getStatus()) 
            && !podeAtualizarStatus(armarioExistente, armarioAtualizado.getStatus())) {
            throw new IllegalArgumentException("Não é possível atualizar o status do armário para " + armarioAtualizado.getStatus());
        }

        // Atualiza os campos permitidos
        armarioExistente.setNumero(armarioAtualizado.getNumero());
        armarioExistente.setLocalizacao(armarioAtualizado.getLocalizacao());
        armarioExistente.setStatus(armarioAtualizado.getStatus());
        armarioExistente.setObservacoes(armarioAtualizado.getObservacoes());

        logger.info("Atualizando armário com ID: {}", id);
        return armarioRepository.save(armarioExistente);
    }

    /**
     * Remove um armário do sistema.
     * Verifica se o armário pode ser removido antes de realizar a operação.
     * 
     * @param id ID do armário a ser removido
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se o armário não for encontrado
     * @throws IllegalStateException se o armário não puder ser removido
     */
    @Transactional
    public void remover(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do armário não pode ser nulo");
        }

        Armario armario = armarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Armário não encontrado com ID: " + id));

        // Verifica se o armário pode ser removido
        if (armario.isOcupado()) {
            throw new IllegalStateException("Não é possível remover um armário ocupado");
        }

        if (armario.getEncomendaAtual() != null) {
            throw new IllegalStateException("Não é possível remover um armário com encomenda atual");
        }

        logger.info("Removendo armário com ID: {}", id);
        armarioRepository.deleteById(id);
    }

    /**
     * Busca armários por número.
     * 
     * @param numero Número do armário
     * @return Optional contendo o armário encontrado, ou vazio se não existir
     * @throws IllegalArgumentException se o número for nulo ou vazio
     */
    public Optional<Armario> buscarPorNumero(String numero) {
        if (!StringUtils.hasText(numero)) {
            throw new IllegalArgumentException("Número do armário não pode ser nulo ou vazio");
        }
        logger.debug("Buscando armário com número: {}", numero);
        return armarioRepository.findByNumero(numero);
    }

    /**
     * Verifica se um armário está disponível para uso.
     * 
     * @param id ID do armário
     * @return true se o armário estiver disponível, false caso contrário
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se o armário não for encontrado
     */
    public boolean estaDisponivel(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do armário não pode ser nulo");
        }

        Armario armario = armarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Armário não encontrado com ID: " + id));

        return armario.isDisponivel();
    }
} 