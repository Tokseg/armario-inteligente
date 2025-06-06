package br.com.unit.tokseg.armario_inteligente.controller;

import br.com.unit.tokseg.armario_inteligente.model.Armario;
import br.com.unit.tokseg.armario_inteligente.model.ArmarioStatus;
import br.com.unit.tokseg.armario_inteligente.model.Compartimento;
import br.com.unit.tokseg.armario_inteligente.service.CompartimentoService;
import br.com.unit.tokseg.armario_inteligente.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompartimentoController.class)
@Import(TestSecurityConfig.class)
class CompartimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompartimentoService compartimentoService;

    private Compartimento compartimento;
    private Armario armario;
    private UUID compartimentoId;
    private UUID armarioId;

    @BeforeEach
    void setUp() {
        compartimentoId = UUID.randomUUID();
        armarioId = UUID.randomUUID();
        
        armario = Armario.builder()
                .id(armarioId)
                .numero("A001")
                .status(ArmarioStatus.DISPONIVEL)
                .localizacao("Bloco A")
                .build();

        compartimento = Compartimento.builder()
                .id(compartimentoId)
                .numero("C001")
                .tamanho(10.0)
                .status(Compartimento.StatusCompartimento.DISPONIVEL)
                .armario(armario)
                .build();
    }

    @Test
    @WithMockUser
    void listarTodos_DeveRetornarListaDeCompartimentos() throws Exception {
        when(compartimentoService.listarTodos()).thenReturn(Arrays.asList(compartimento));

        mockMvc.perform(get("/api/compartimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(compartimentoId.toString()))
                .andExpect(jsonPath("$[0].numero").value("C001"))
                .andExpect(jsonPath("$[0].tamanho").value(10.0))
                .andExpect(jsonPath("$[0].status").value("DISPONIVEL"));

        verify(compartimentoService).listarTodos();
    }

    @Test
    @WithMockUser
    void buscarPorId_QuandoExiste_DeveRetornarCompartimento() throws Exception {
        when(compartimentoService.buscarPorId(compartimentoId)).thenReturn(Optional.of(compartimento));

        mockMvc.perform(get("/api/compartimentos/{id}", compartimentoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(compartimentoId.toString()))
                .andExpect(jsonPath("$.numero").value("C001"))
                .andExpect(jsonPath("$.tamanho").value(10.0))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));

        verify(compartimentoService).buscarPorId(compartimentoId);
    }

    @Test
    @WithMockUser
    void buscarPorId_QuandoNaoExiste_DeveRetornar404() throws Exception {
        when(compartimentoService.buscarPorId(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/compartimentos/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());

        verify(compartimentoService).buscarPorId(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void criar_QuandoDadosValidos_DeveCriarCompartimento() throws Exception {
        when(compartimentoService.salvar(any(Compartimento.class))).thenReturn(compartimento);

        mockMvc.perform(post("/api/compartimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compartimento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(compartimentoId.toString()))
                .andExpect(jsonPath("$.numero").value("C001"));

        verify(compartimentoService).salvar(any(Compartimento.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizar_QuandoDadosValidos_DeveAtualizarCompartimento() throws Exception {
        when(compartimentoService.existePorId(compartimentoId)).thenReturn(true);
        when(compartimentoService.salvar(any(Compartimento.class))).thenReturn(compartimento);

        mockMvc.perform(put("/api/compartimentos/{id}", compartimentoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compartimento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(compartimentoId.toString()))
                .andExpect(jsonPath("$.numero").value("C001"));

        verify(compartimentoService).existePorId(compartimentoId);
        verify(compartimentoService).salvar(any(Compartimento.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizar_QuandoNaoExiste_DeveRetornar404() throws Exception {
        when(compartimentoService.existePorId(any())).thenReturn(false);

        mockMvc.perform(put("/api/compartimentos/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compartimento)))
                .andExpect(status().isNotFound());

        verify(compartimentoService).existePorId(any());
        verify(compartimentoService, never()).salvar(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void remover_QuandoExiste_DeveRemoverCompartimento() throws Exception {
        when(compartimentoService.existePorId(compartimentoId)).thenReturn(true);
        doNothing().when(compartimentoService).remover(compartimentoId);

        mockMvc.perform(delete("/api/compartimentos/{id}", compartimentoId))
                .andExpect(status().isNoContent());

        verify(compartimentoService).existePorId(compartimentoId);
        verify(compartimentoService).remover(compartimentoId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void remover_QuandoNaoExiste_DeveRetornar404() throws Exception {
        when(compartimentoService.existePorId(any())).thenReturn(false);

        mockMvc.perform(delete("/api/compartimentos/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());

        verify(compartimentoService).existePorId(any());
        verify(compartimentoService, never()).remover(any());
    }

    @Test
    @WithMockUser
    void buscarPorArmario_DeveRetornarListaDeCompartimentos() throws Exception {
        when(compartimentoService.buscarPorArmario(armarioId)).thenReturn(Arrays.asList(compartimento));

        mockMvc.perform(get("/api/compartimentos/armario/{armarioId}", armarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(compartimentoId.toString()))
                .andExpect(jsonPath("$[0].numero").value("C001"));

        verify(compartimentoService).buscarPorArmario(armarioId);
    }
}