package br.com.unit.tokseg.armario_inteligente.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import br.com.unit.tokseg.armario_inteligente.service.JwtService;
import br.com.unit.tokseg.armario_inteligente.service.CustomUserDetailsService;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }
} 