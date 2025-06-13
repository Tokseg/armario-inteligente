package br.com.unit.tokseg.armario_inteligente.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.unit.tokseg.armario_inteligente.repository")
@EnableTransactionManagement
public class JpaConfiguration {
    // Configuração explícita do JPA
} 