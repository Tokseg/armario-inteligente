package br.com.unit.tokseg.armario_inteligente.config;

import br.com.unit.tokseg.armario_inteligente.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Filtro responsável por interceptar e processar requisições HTTP para autenticação JWT.
 * Este filtro é executado uma vez por requisição e verifica a presença e validade do token JWT.
 * 
 * O filtro:
 * 1. Intercepta todas as requisições HTTP
 * 2. Verifica se o cabeçalho Authorization contém um token JWT
 * 3. Valida o token e extrai as informações do usuário
 * 4. Configura o contexto de segurança com as informações do usuário autenticado
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Construtor que recebe as dependências necessárias via injeção de dependência.
     * 
     * @param jwtService Serviço JWT para manipulação de tokens
     * @param userDetailsService Serviço para carregar detalhes do usuário
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.debug("Cabeçalho de autorização não encontrado ou inválido");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(BEARER_PREFIX.length());
            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.debug("Processando token JWT para usuário: {}", userEmail);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    logger.info("Token JWT válido para usuário: {}", userEmail);
                    
                    // Extrair authorities do token
                    @SuppressWarnings("unchecked")
                    List<String> authorities = jwtService.extractClaim(jwt, claims -> 
                        (List<String>) claims.get("authorities"));
                    
                    // Se não houver authorities no token, usar as do userDetails
                    Collection<? extends GrantedAuthority> grantedAuthorities = authorities != null ?
                        authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()) :
                        userDetails.getAuthorities();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            grantedAuthorities
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.warn("Token JWT inválido para usuário: {}", userEmail);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao processar token JWT: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
} 