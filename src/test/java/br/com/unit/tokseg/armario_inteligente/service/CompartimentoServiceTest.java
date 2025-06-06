package br.com.unit.tokseg.armario_inteligente.service;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import br.com.unit.tokseg.armario_inteligente.model.Compartimento;
import br.com.unit.tokseg.armario_inteligente.repository.CompartimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompartimentoServiceTest {

    @Mock
    private CompartimentoRepository compartimentoRepository;

    @InjectMocks
    private CompartimentoService compartimentoService;

    private Compartimento compartimento;
    private Armario armario;
    private UUID compartimentoId;
    private UUID armarioId;

    @BeforeEach
    void setUp() {
        compartimentoId = UUID.randomUUID();
        armarioId = UUID.randomUUID();
        
        armario = new Armario();
        armario.setId(armarioId);
        armario.setNumero("A001");
        armario.setStatus(ArmarioStatus.DISPONIVEL);
        armario.setLocalizacao("Bloco A");

        compartimento = new Compartimento();
        compartimento.setId(compartimentoId);
        compartimento.setNumero("C001");
        compartimento.setTamanho(10.0);
        compartimento.setStatus(Compartimento.StatusCompartimento.DISPONIVEL);
        compartimento.setArmario(armario);
    }

    @Test
    void listarTodos_DeveRetornarListaDeCompartimentos() {
        when(compartimentoRepository.findAll()).thenReturn(Arrays.asList(compartimento));

        List<Compartimento> resultado = compartimentoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(compartimento, resultado.get(0));
        verify(compartimentoRepository).findAll();
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarCompartimento() {
        when(compartimentoRepository.findById(compartimentoId)).thenReturn(Optional.of(compartimento));

        Optional<Compartimento> resultado = compartimentoService.buscarPorId(compartimentoId);

        assertTrue(resultado.isPresent());
        assertEquals(compartimento, resultado.get());
        verify(compartimentoRepository).findById(compartimentoId);
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveRetornarVazio() {
        when(compartimentoRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Compartimento> resultado = compartimentoService.buscarPorId(UUID.randomUUID());

        assertTrue(resultado.isEmpty());
        verify(compartimentoRepository).findById(any());
    }

    @Test
    void buscarPorId_QuandoIdNulo_DeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> compartimentoService.buscarPorId(null));
        verify(compartimentoRepository, never()).findById(any());
    }

    @Test
    void buscarPorArmario_DeveRetornarListaDeCompartimentos() {
        when(compartimentoRepository.findByArmarioId(armarioId)).thenReturn(Arrays.asList(compartimento));

        List<Compartimento> resultado = compartimentoService.buscarPorArmario(armarioId);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(compartimento, resultado.get(0));
        verify(compartimentoRepository).findByArmarioId(armarioId);
    }

    @Test
    void buscarPorArmario_QuandoArmarioIdNulo_DeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> compartimentoService.buscarPorArmario(null));
        verify(compartimentoRepository, never()).findByArmarioId(any());
    }

    @Test
    void salvar_QuandoDadosValidos_DeveSalvarCompartimento() {
        when(compartimentoRepository.save(any(Compartimento.class))).thenReturn(compartimento);

        Compartimento resultado = compartimentoService.salvar(compartimento);

        assertNotNull(resultado);
        assertEquals(compartimento, resultado);
        verify(compartimentoRepository).save(compartimento);
    }

    @Test
    void salvar_QuandoCompartimentoNulo_DeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> compartimentoService.salvar(null));
        verify(compartimentoRepository, never()).save(any());
    }

    @Test
    void salvar_QuandoArmarioNulo_DeveLancarExcecao() {
        compartimento.setArmario(null);
        assertThrows(IllegalArgumentException.class, () -> compartimentoService.salvar(compartimento));
        verify(compartimentoRepository, never()).save(any());
    }

    @Test
    void remover_QuandoExiste_DeveRemoverCompartimento() {
        when(compartimentoRepository.existsById(compartimentoId)).thenReturn(true);
        doNothing().when(compartimentoRepository).deleteById(compartimentoId);

        assertDoesNotThrow(() -> compartimentoService.remover(compartimentoId));
        verify(compartimentoRepository).existsById(compartimentoId);
        verify(compartimentoRepository).deleteById(compartimentoId);
    }

    @Test
    void remover_QuandoNaoExiste_DeveLancarExcecao() {
        when(compartimentoRepository.existsById(any())).thenReturn(false);

        assertThrows(br.com.unit.tokseg.armario_inteligente.exception.ResourceNotFoundException.class, () -> compartimentoService.remover(UUID.randomUUID()));
        verify(compartimentoRepository).existsById(any());
        verify(compartimentoRepository, never()).deleteById(any());
    }

    @Test
    void remover_QuandoIdNulo_DeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> compartimentoService.remover(null));
        verify(compartimentoRepository, never()).existsById(any());
        verify(compartimentoRepository, never()).deleteById(any());
    }

    @Test
    void atualizarOcupacao_QuandoExiste_DeveAtualizarStatus() {
        when(compartimentoRepository.findById(compartimentoId)).thenReturn(Optional.of(compartimento));
        when(compartimentoRepository.save(any(Compartimento.class))).thenReturn(compartimento);

        Optional<Compartimento> resultado = compartimentoService.atualizarOcupacao(compartimentoId, true);

        assertTrue(resultado.isPresent());
        assertEquals(Compartimento.StatusCompartimento.OCUPADO, resultado.get().getStatus());
        verify(compartimentoRepository).findById(compartimentoId);
        verify(compartimentoRepository).save(any(Compartimento.class));
    }

    @Test
    void atualizarOcupacao_QuandoNaoExiste_DeveRetornarVazio() {
        when(compartimentoRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Compartimento> resultado = compartimentoService.atualizarOcupacao(UUID.randomUUID(), true);

        assertTrue(resultado.isEmpty());
        verify(compartimentoRepository).findById(any());
        verify(compartimentoRepository, never()).save(any());
    }

    @Test
    void atualizarOcupacao_QuandoIdNulo_DeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> compartimentoService.atualizarOcupacao(null, true));
        verify(compartimentoRepository, never()).findById(any());
        verify(compartimentoRepository, never()).save(any());
    }
} 