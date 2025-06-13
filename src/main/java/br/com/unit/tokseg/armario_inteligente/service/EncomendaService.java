package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Encomenda;
import br.com.unit.tokseg.armario_inteligente.repository.EncomendaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import br.com.unit.tokseg.armario_inteligente.annotation.Auditavel;

/**
 * Serviço responsável pela lógica de negócios relacionada às encomendas.
 * Implementa as operações de CRUD e regras específicas do domínio.
 */
@Service
public class EncomendaService {

    private static final Logger logger = LoggerFactory.getLogger(EncomendaService.class);
    private final EncomendaRepository encomendaRepository;

    /**
     * Construtor explícito para inicializar o repositório de encomenda.
     * @param encomendaRepository Repositório de encomenda a ser injetado
     */
    public EncomendaService(EncomendaRepository encomendaRepository) {
        this.encomendaRepository = encomendaRepository;
    }

    /**
     * Salva uma nova encomenda no sistema.
     * 
     * @param encomenda Encomenda a ser salva
     * @return Encomenda salva com ID gerado
     * @throws IllegalArgumentException se a encomenda for nula ou inválida
     */
    @Auditavel(acao = "CADASTRO_ENCOMENDA", detalhes = "Cadastro de nova encomenda no sistema")
    @Transactional
    public Encomenda salvar(Encomenda encomenda) {
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não pode ser nula");
        }
        if (encomenda.getIdEncomenda() == null || encomenda.getIdEncomenda().trim().isEmpty()) {
            throw new IllegalArgumentException("ID da encomenda não pode ser nulo ou vazio");
        }
        if (encomenda.getDescricao() == null || encomenda.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição da encomenda não pode ser nula ou vazia");
        }
        if (encomenda.getRemetente() == null || encomenda.getRemetente().trim().isEmpty()) {
            throw new IllegalArgumentException("Remetente não pode ser nulo ou vazio");
        }

        logger.info("Salvando nova encomenda com ID: {}", encomenda.getIdEncomenda());
        return encomendaRepository.save(encomenda);
    }

    /**
     * Lista todas as encomendas cadastradas no sistema.
     * 
     * @return Lista de todas as encomendas
     */
    public List<Encomenda> listarTodas() {
        logger.debug("Listando todas as encomendas");
        return encomendaRepository.findAll();
    }

    /**
     * Busca uma encomenda específica pelo ID.
     * 
     * @param id ID da encomenda a ser buscada
     * @return Optional contendo a encomenda encontrada, ou vazio se não existir
     * @throws IllegalArgumentException se o ID for nulo ou vazio
     */
    public Optional<Encomenda> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID da encomenda não pode ser nulo ou vazio");
        }
        logger.debug("Buscando encomenda com ID: {}", id);
        return encomendaRepository.findById(id);
    }

    /**
     * Remove uma encomenda do sistema.
     * 
     * @param id ID da encomenda a ser removida
     * @throws IllegalArgumentException se o ID for nulo ou vazio
     * @throws EntityNotFoundException se a encomenda não for encontrada
     */
    @Auditavel(acao = "RETIRADA_ENCOMENDA", detalhes = "Retirada de encomenda do sistema")
    @Transactional
    public void remover(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID da encomenda não pode ser nulo ou vazio");
        }
        
        if (!encomendaRepository.existsById(id)) {
            throw new EntityNotFoundException("Encomenda não encontrada com ID: " + id);
        }

        logger.info("Removendo encomenda com ID: {}", id);
        encomendaRepository.deleteById(id);
    }
} 