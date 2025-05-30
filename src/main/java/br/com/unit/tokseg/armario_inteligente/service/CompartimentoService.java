package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Compartimento;
import br.com.unit.tokseg.armario_inteligente.repository.CompartimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócios relacionada aos compartimentos.
 * Implementa as operações de CRUD e regras específicas do domínio.
 */
@Service
public class CompartimentoService {

    private static final Logger logger = LoggerFactory.getLogger(CompartimentoService.class);
    private final CompartimentoRepository compartimentoRepository;

    /**
     * Construtor explícito para inicializar o repositório de compartimento.
     * @param compartimentoRepository Repositório de compartimento a ser injetado
     */
    public CompartimentoService(CompartimentoRepository compartimentoRepository) {
        this.compartimentoRepository = compartimentoRepository;
    }

    /**
     * Lista todos os compartimentos cadastrados no sistema.
     * 
     * @return Lista de todos os compartimentos
     */
    public List<Compartimento> listarTodos() {
        logger.debug("Listando todos os compartimentos");
        return compartimentoRepository.findAll();
    }

    /**
     * Busca um compartimento específico pelo ID.
     * 
     * @param id ID do compartimento a ser buscado
     * @return Optional contendo o compartimento encontrado, ou vazio se não existir
     * @throws IllegalArgumentException se o ID for nulo
     */
    public Optional<Compartimento> buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do compartimento não pode ser nulo");
        }
        logger.debug("Buscando compartimento com ID: {}", id);
        return compartimentoRepository.findById(id);
    }

    /**
     * Salva um novo compartimento no sistema.
     * 
     * @param compartimento Compartimento a ser salvo
     * @return Compartimento salvo com ID gerado
     * @throws IllegalArgumentException se o compartimento for nulo ou inválido
     */
    @Transactional
    public Compartimento salvar(Compartimento compartimento) {
        if (compartimento == null) {
            throw new IllegalArgumentException("Compartimento não pode ser nulo");
        }
        if (compartimento.getArmario() == null) {
            throw new IllegalArgumentException("Armário não pode ser nulo");
        }

        logger.info("Salvando novo compartimento para o armário: {}", compartimento.getArmario().getId());
        return compartimentoRepository.save(compartimento);
    }

    /**
     * Remove um compartimento do sistema.
     * 
     * @param id ID do compartimento a ser removido
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se o compartimento não for encontrado
     */
    @Transactional
    public void remover(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do compartimento não pode ser nulo");
        }
        
        if (!compartimentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Compartimento não encontrado com ID: " + id);
        }

        logger.info("Removendo compartimento com ID: {}", id);
        compartimentoRepository.deleteById(id);
    }

    /**
     * Atualiza o status de ocupação de um compartimento.
     * 
     * @param id ID do compartimento a ser atualizado
     * @param ocupado Novo status de ocupação
     * @return Optional contendo o compartimento atualizado, ou vazio se não encontrado
     * @throws IllegalArgumentException se o ID for nulo
     */
    @Transactional
    public Optional<Compartimento> atualizarOcupacao(UUID id, boolean ocupado) {
        if (id == null) {
            throw new IllegalArgumentException("ID do compartimento não pode ser nulo");
        }

        logger.info("Atualizando status de ocupação do compartimento {} para {}", id, ocupado);
        return compartimentoRepository.findById(id)
                .map(compartimento -> {
                    compartimento.setOcupado(ocupado);
                    return compartimentoRepository.save(compartimento);
                });
    }
} 