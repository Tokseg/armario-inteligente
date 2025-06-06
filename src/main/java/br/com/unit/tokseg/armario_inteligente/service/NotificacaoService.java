package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Notificacao;
import br.com.unit.tokseg.armario_inteligente.repository.NotificacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócios relacionada às notificações.
 * Implementa as operações de CRUD e regras específicas do domínio.
 */
@Service
@Transactional
public class NotificacaoService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoService.class);
    private final NotificacaoRepository notificacaoRepository;

    /**
     * Construtor explícito para inicializar o repositório de notificação.
     * @param notificacaoRepository Repositório de notificação a ser injetado
     */
    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    /**
     * Salva uma nova notificação no sistema.
     * 
     * @param notificacao Notificação a ser salva
     * @return Notificação salva com ID gerado
     * @throws IllegalArgumentException se a notificação for nula ou inválida
     */
    public Notificacao salvar(Notificacao notificacao) {
        if (notificacao == null) {
            throw new IllegalArgumentException("Notificação não pode ser nula");
        }
        if (notificacao.getTitulo() == null || notificacao.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título da notificação não pode ser nulo ou vazio");
        }
        if (notificacao.getMensagem() == null || notificacao.getMensagem().trim().isEmpty()) {
            throw new IllegalArgumentException("Mensagem da notificação não pode ser nula ou vazia");
        }
        if (notificacao.getTipoNotificacao() == null) {
            throw new IllegalArgumentException("Tipo da notificação não pode ser nulo");
        }
        if (notificacao.getUsuario() == null) {
            throw new IllegalArgumentException("Usuário da notificação não pode ser nulo");
        }

        logger.info("Salvando nova notificação com título: {}", notificacao.getTitulo());
        return notificacaoRepository.save(notificacao);
    }

    /**
     * Lista todas as notificações cadastradas no sistema.
     * 
     * @return Lista de todas as notificações
     */
    public List<Notificacao> listarTodas() {
        logger.debug("Listando todas as notificações");
        return notificacaoRepository.findAll();
    }

    /**
     * Busca uma notificação específica pelo ID.
     * 
     * @param id ID da notificação a ser buscada
     * @return Optional contendo a notificação encontrada, ou vazio se não existir
     * @throws IllegalArgumentException se o ID for nulo
     */
    public Optional<Notificacao> buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da notificação não pode ser nulo");
        }
        logger.debug("Buscando notificação com ID: {}", id);
        return notificacaoRepository.findById(id);
    }

    /**
     * Remove uma notificação do sistema.
     * 
     * @param id ID da notificação a ser removida
     * @throws IllegalArgumentException se o ID for nulo
     * @throws EntityNotFoundException se a notificação não for encontrada
     */
    public void remover(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da notificação não pode ser nulo");
        }
        
        if (!notificacaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Notificação não encontrada com ID: " + id);
        }

        logger.info("Removendo notificação com ID: {}", id);
        notificacaoRepository.deleteById(id);
    }

    /**
     * Busca notificações de um usuário específico.
     * 
     * @param usuarioId ID do usuário cujas notificações serão buscadas
     * @return Lista de notificações do usuário
     */
    public List<Notificacao> buscarPorUsuario(UUID usuarioId) {
        return notificacaoRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Busca notificações não lidas de um usuário específico.
     * 
     * @param usuarioId ID do usuário cujas notificações serão buscadas
     * @return Lista de notificações não lidas do usuário
     */
    public List<Notificacao> buscarNaoLidasPorUsuario(UUID usuarioId) {
        return notificacaoRepository.findByUsuarioIdAndLidaFalse(usuarioId);
    }

    /**
     * Marca uma notificação como lida.
     * 
     * @param id ID da notificação a ser marcada como lida
     * @return Optional contendo a notificação atualizada, ou vazio se não existir
     */
    public Optional<Notificacao> marcarComoLida(UUID id) {
        return notificacaoRepository.findById(id)
                .map(notificacao -> {
                    notificacao.setLida(true);
                    notificacao.setDataLeitura(LocalDateTime.now());
                    return notificacaoRepository.save(notificacao);
                });
    }

    /**
     * Verifica se uma notificação existe pelo ID.
     * 
     * @param id ID da notificação a ser verificada
     * @return true se a notificação existir, false caso contrário
     */
    public boolean existePorId(UUID id) {
        return notificacaoRepository.existsById(id);
    }
} 