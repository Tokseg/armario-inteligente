package br.com.unit.tokseg.armario_inteligente;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import br.com.unit.tokseg.armario_inteligente.config.TestSecurityConfig;

/**
 * Classe de teste para verificar se o contexto da aplicação carrega corretamente.
 * Utiliza o perfil de teste e configurações específicas para testes.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(TestSecurityConfig.class)
class ArmarioInteligenteApplicationTests {

	/**
	 * Testa se o contexto da aplicação carrega corretamente.
	 * Este teste verifica se todas as configurações necessárias estão presentes
	 * e se os beans do Spring são inicializados corretamente.
	 */
	@Test
	void contextLoads() {
		// O teste passa se o contexto carregar sem exceções
	}

}
