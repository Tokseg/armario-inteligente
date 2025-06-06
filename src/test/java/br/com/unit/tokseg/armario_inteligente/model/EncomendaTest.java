package br.com.unit.tokseg.armario_inteligente.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EncomendaTest {

    private Validator validator;
    private UUID armarioId;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        armarioId = UUID.randomUUID();
        usuarioId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve criar uma encomenda válida")
    void deveCriarEncomendaValida() {
        Encomenda encomenda = Encomenda.builder()
                .codigoRastreio("ABC123")
                .descricao("Pacote pequeno")
                .remetente("Correios")
                .dataRecebimento(LocalDateTime.now())
                .armario(Armario.builder().id(armarioId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();

        var violations = validator.validate(encomenda);
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    @DisplayName("Não deve aceitar código de rastreio nulo")
    void naoDeveAceitarCodigoRastreioNulo() {
        Encomenda encomenda = Encomenda.builder()
                .descricao("Pacote pequeno")
                .remetente("Correios")
                .dataRecebimento(LocalDateTime.now())
                .armario(Armario.builder().id(armarioId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();

        var violations = validator.validate(encomenda);
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("codigoRastreio")));
    }

    @Test
    @DisplayName("Não deve aceitar código de rastreio muito curto")
    void naoDeveAceitarCodigoRastreioMuitoCurto() {
        Encomenda encomenda = Encomenda.builder()
                .codigoRastreio("ABC")
                .descricao("Pacote pequeno")
                .remetente("Correios")
                .dataRecebimento(LocalDateTime.now())
                .armario(Armario.builder().id(armarioId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();

        var violations = validator.validate(encomenda);
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("codigoRastreio")));
    }

    @Test
    @DisplayName("Não deve aceitar descrição nula")
    void naoDeveAceitarDescricaoNula() {
        Encomenda encomenda = Encomenda.builder()
                .codigoRastreio("ABC123")
                .remetente("Correios")
                .dataRecebimento(LocalDateTime.now())
                .armario(Armario.builder().id(armarioId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();

        var violations = validator.validate(encomenda);
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("descricao")));
    }

    @Test
    @DisplayName("Não deve aceitar remetente nulo")
    void naoDeveAceitarRemetenteNulo() {
        Encomenda encomenda = Encomenda.builder()
                .codigoRastreio("ABC123")
                .descricao("Pacote pequeno")
                .dataRecebimento(LocalDateTime.now())
                .armario(Armario.builder().id(armarioId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();

        var violations = validator.validate(encomenda);
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("remetente")));
    }

    @Test
    @DisplayName("Não deve aceitar data de recebimento nula")
    void naoDeveAceitarDataRecebimentoNula() {
        Encomenda encomenda = Encomenda.builder()
                .codigoRastreio("ABC123")
                .descricao("Pacote pequeno")
                .remetente("Correios")
                .armario(Armario.builder().id(armarioId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();

        var violations = validator.validate(encomenda);
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dataRecebimento")));
    }

    @Test
    @DisplayName("Não deve aceitar armário nulo")
    void naoDeveAceitarArmarioNulo() {
        Encomenda encomenda = Encomenda.builder()
                .codigoRastreio("ABC123")
                .descricao("Pacote pequeno")
                .remetente("Correios")
                .dataRecebimento(LocalDateTime.now())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();

        var violations = validator.validate(encomenda);
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("armario")));
    }

    @Test
    @DisplayName("Não deve aceitar usuário nulo")
    void naoDeveAceitarUsuarioNulo() {
        Encomenda encomenda = Encomenda.builder()
                .codigoRastreio("ABC123")
                .descricao("Pacote pequeno")
                .remetente("Correios")
                .dataRecebimento(LocalDateTime.now())
                .armario(Armario.builder().id(armarioId).build())
                .build();

        var violations = validator.validate(encomenda);
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("usuario")));
    }

    @Test
    @DisplayName("Deve confirmar retirada corretamente")
    void deveConfirmarRetiradaCorretamente() {
        Encomenda encomenda = Encomenda.builder()
                .codigoRastreio("ABC123")
                .descricao("Pacote pequeno")
                .remetente("Correios")
                .dataRecebimento(LocalDateTime.now())
                .armario(Armario.builder().id(armarioId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .build();

        assertFalse(encomenda.isRetiradaConfirmada());
        assertNull(encomenda.getDataRetirada());

        encomenda.setRetiradaConfirmada(true);
        encomenda.setDataRetirada(LocalDateTime.now());

        assertTrue(encomenda.isRetiradaConfirmada());
        assertNotNull(encomenda.getDataRetirada());
    }
} 