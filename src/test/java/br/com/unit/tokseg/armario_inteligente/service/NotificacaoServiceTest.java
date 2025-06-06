package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Notificacao;
import br.com.unit.tokseg.armario_inteligente.model.Notificacao.TipoNotificacao;
import br.com.unit.tokseg.armario_inteligente.model.Usuario;
import br.com.unit.tokseg.armario_inteligente.repository.NotificacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private Notificacao notificacao;
    private UUID notificacaoId;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        notificacaoId = UUID.randomUUID();
        usuarioId = UUID.randomUUID();

        notificacao = Notificacao.builder()
                .id(notificacaoId)
                .titulo("Nova encomenda")
                .mensagem("Você tem uma nova encomenda disponível")
                .tipoNotificacao(TipoNotificacao.ENCOMENDA)
                .dataCriacao(LocalDateTime.now())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();
    }

    @Test
    @DisplayName("Deve salvar uma notificação com sucesso")
    void deveSalvarNotificacaoComSucesso() {
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        Notificacao notificacaoSalva = notificacaoService.salvar(notificacao);

        assertNotNull(notificacaoSalva);
        assertEquals(notificacaoId, notificacaoSalva.getId());
        verify(notificacaoRepository, times(1)).save(notificacao);
    }

    @Test
    @DisplayName("Não deve salvar notificação nula")
    void naoDeveSalvarNotificacaoNula() {
        assertThrows(IllegalArgumentException.class, () -> notificacaoService.salvar(null));
        verify(notificacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todas as notificações")
    void deveListarTodasNotificacoes() {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        when(notificacaoRepository.findAll()).thenReturn(notificacoes);

        List<Notificacao> resultado = notificacaoService.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacaoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar notificação por ID")
    void deveBuscarNotificacaoPorId() {
        when(notificacaoRepository.findById(notificacaoId)).thenReturn(Optional.of(notificacao));

        Optional<Notificacao> resultado = notificacaoService.buscarPorId(notificacaoId);

        assertTrue(resultado.isPresent());
        assertEquals(notificacaoId, resultado.get().getId());
        verify(notificacaoRepository, times(1)).findById(notificacaoId);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar notificação inexistente")
    void deveRetornarVazioAoBuscarNotificacaoInexistente() {
        when(notificacaoRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Notificacao> resultado = notificacaoService.buscarPorId(UUID.randomUUID());

        assertTrue(resultado.isEmpty());
        verify(notificacaoRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Deve remover notificação com sucesso")
    void deveRemoverNotificacaoComSucesso() {
        when(notificacaoRepository.existsById(notificacaoId)).thenReturn(true);
        doNothing().when(notificacaoRepository).deleteById(notificacaoId);

        assertDoesNotThrow(() -> notificacaoService.remover(notificacaoId));
        verify(notificacaoRepository, times(1)).deleteById(notificacaoId);
    }

    @Test
    @DisplayName("Não deve remover notificação inexistente")
    void naoDeveRemoverNotificacaoInexistente() {
        when(notificacaoRepository.existsById(notificacaoId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> notificacaoService.remover(notificacaoId));
        verify(notificacaoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve buscar notificações por usuário")
    void deveBuscarNotificacoesPorUsuario() {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        when(notificacaoRepository.findByUsuarioId(usuarioId)).thenReturn(notificacoes);

        List<Notificacao> resultado = notificacaoService.buscarPorUsuario(usuarioId);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacaoRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Deve buscar notificações não lidas por usuário")
    void deveBuscarNotificacoesNaoLidasPorUsuario() {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        when(notificacaoRepository.findByUsuarioIdAndLidaFalse(usuarioId)).thenReturn(notificacoes);

        List<Notificacao> resultado = notificacaoService.buscarNaoLidasPorUsuario(usuarioId);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacaoRepository, times(1)).findByUsuarioIdAndLidaFalse(usuarioId);
    }

    @Test
    @DisplayName("Deve marcar notificação como lida")
    void deveMarcarNotificacaoComoLida() {
        when(notificacaoRepository.findById(notificacaoId)).thenReturn(Optional.of(notificacao));
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        Optional<Notificacao> resultado = notificacaoService.marcarComoLida(notificacaoId);

        assertTrue(resultado.isPresent());
        assertTrue(resultado.get().isLida());
        assertNotNull(resultado.get().getDataLeitura());
        verify(notificacaoRepository, times(1)).findById(notificacaoId);
        verify(notificacaoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve verificar existência de notificação por ID")
    void deveVerificarExistenciaDeNotificacaoPorId() {
        when(notificacaoRepository.existsById(notificacaoId)).thenReturn(true);

        boolean resultado = notificacaoService.existePorId(notificacaoId);

        assertTrue(resultado);
        verify(notificacaoRepository, times(1)).existsById(notificacaoId);
    }
} 