package br.com.unit.tokseg.armario_inteligente.aspect;

import br.com.unit.tokseg.armario_inteligente.annotation.Auditavel;
import br.com.unit.tokseg.armario_inteligente.model.*;
import br.com.unit.tokseg.armario_inteligente.service.NotificacaoService;
import br.com.unit.tokseg.armario_inteligente.service.RegistroAuditoriaService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
public class AuditoriaAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditoriaAspect.class);
    private final RegistroAuditoriaService registroAuditoriaService;
    private final NotificacaoService notificacaoService;

    public AuditoriaAspect(
            RegistroAuditoriaService registroAuditoriaService,
            NotificacaoService notificacaoService) {
        this.registroAuditoriaService = registroAuditoriaService;
        this.notificacaoService = notificacaoService;
    }

    @Around("@annotation(auditavel)")
    public Object registrarAuditoria(ProceedingJoinPoint joinPoint, Auditavel auditavel) throws Throwable {
        String acao = auditavel.acao().isEmpty() ? 
            joinPoint.getSignature().getName() : auditavel.acao();
        
        String detalhes = auditavel.detalhes().isEmpty() ?
            String.format("Método: %s, Classe: %s", 
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName()) :
            auditavel.detalhes();

        RegistroAuditoria registro = new RegistroAuditoria();
        registro.setAcao(acao);
        registro.setDetalhes(detalhes);
        registro.setDataHora(LocalDateTime.now());

        try {
            Object resultado = joinPoint.proceed();
            
            // Registra a auditoria
            registroAuditoriaService.salvar(registro);
            
            // Cria notificação se necessário
            criarNotificacaoSeNecessario(joinPoint, acao, resultado);
            
            return resultado;
        } catch (Exception e) {
            registro.setDetalhes(registro.getDetalhes() + " - Erro: " + e.getMessage());
            registroAuditoriaService.salvar(registro);
            throw e;
        }
    }

    private void criarNotificacaoSeNecessario(ProceedingJoinPoint joinPoint, String acao, Object resultado) {
        try {
            if (resultado instanceof Usuario) {
                Usuario usuario = (Usuario) resultado;
                if (acao.contains("REGISTRO_USUARIO")) {
                    criarNotificacaoAdmin("Novo usuário registrado: " + usuario.getEmail());
                }
            } else if (resultado instanceof Armario) {
                Armario armario = (Armario) resultado;
                if (acao.contains("CADASTRO_ARMARIO")) {
                    criarNotificacaoAdmin("Novo armário cadastrado: " + armario.getNumero());
                }
            } else if (resultado instanceof Encomenda) {
                Encomenda encomenda = (Encomenda) resultado;
                if (acao.contains("CADASTRO_ENCOMENDA")) {
                    criarNotificacaoUsuario(encomenda.getUsuario(), 
                        "Nova encomenda registrada para você no armário " + 
                        encomenda.getArmario().getNumero());
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao criar notificação: {}", e.getMessage(), e);
        }
    }

    private void criarNotificacaoAdmin(String mensagem) {
        // TODO: Implementar busca de usuários admin e criar notificação para cada um
        logger.info("Notificação para admin: {}", mensagem);
    }

    private void criarNotificacaoUsuario(Usuario usuario, String mensagem) {
        String idNotificacao = UUID.randomUUID().toString();
        LocalDateTime dataEnvio = LocalDateTime.now();
        
        Notificacao notificacao = new Notificacao(
            idNotificacao,
            usuario,
            mensagem,
            dataEnvio
        );
        notificacao.setLida(false);

        notificacaoService.salvar(notificacao);
    }
} 