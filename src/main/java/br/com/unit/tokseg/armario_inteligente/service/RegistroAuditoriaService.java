package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.RegistroAuditoria;
import br.com.unit.tokseg.armario_inteligente.repository.RegistroAuditoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócios relacionada aos registros de auditoria. Implementa as
 * operações de CRUD e regras específicas do domínio.
 * 
 * Este serviço é responsável por: - Registrar todas as ações importantes no sistema - Manter um
 * histórico de operações para fins de auditoria - Fornecer uma interface limpa para consulta de
 * registros
 */
@Service
public class RegistroAuditoriaService {

    private static final Logger logger = LoggerFactory.getLogger(RegistroAuditoriaService.class);
    private final RegistroAuditoriaRepository registroAuditoriaRepository;

    /**
     * Construtor explícito para inicializar o repositório de registros de auditoria.
     * 
     * @param registroAuditoriaRepository Repositório de registros de auditoria a ser injetado
     */
    public RegistroAuditoriaService(RegistroAuditoriaRepository registroAuditoriaRepository) {
        this.registroAuditoriaRepository = registroAuditoriaRepository;
    }

    /**
     * Registra uma nova ação no sistema.
     * 
     * @param registro Registro de auditoria a ser salvo
     * @return Registro salvo com ID gerado
     * @throws IllegalArgumentException se o registro for nulo ou inválido
     */
    @Transactional
    public RegistroAuditoria salvar(RegistroAuditoria registro) {
        if (registro == null) {
            throw new IllegalArgumentException("Registro de auditoria não pode ser nulo");
        }
        if (registro.getAcao() == null || registro.getAcao().trim().isEmpty()) {
            throw new IllegalArgumentException("Ação do registro não pode ser nula ou vazia");
        }
        if (registro.getDataHora() == null) {
            throw new IllegalArgumentException("Data/hora do registro não pode ser nula");
        }

        logger.info("Registrando ação: {}", registro.getAcao());
        return registroAuditoriaRepository.save(registro);
    }

    /**
     * Lista todos os registros de auditoria cadastrados no sistema.
     * 
     * @return Lista de todos os registros
     */
    public List<RegistroAuditoria> listarTodos() {
        logger.debug("Listando todos os registros de auditoria");
        return registroAuditoriaRepository.findAll();
    }

    /**
     * Busca um registro específico pelo ID.
     * 
     * @param id ID do registro a ser buscado
     * @return Optional contendo o registro encontrado, ou vazio se não existir
     * @throws IllegalArgumentException se o ID for nulo
     */
    public Optional<RegistroAuditoria> buscarPorId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do registro não pode ser nulo");
        }
        logger.debug("Buscando registro de auditoria com ID: {}", id);
        return registroAuditoriaRepository.findById(id);
    }

    /**
     * Remove um registro de auditoria do sistema.
     * 
     * @param id ID do registro a ser removido
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se o registro não for encontrado
     */
    @Transactional
    public void remover(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do registro não pode ser nulo");
        }

        if (!registroAuditoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Registro de auditoria não encontrado com ID: " + id);
        }

        logger.info("Removendo registro de auditoria com ID: {}", id);
        registroAuditoriaRepository.deleteById(id);
    }
}
