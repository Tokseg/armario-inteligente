package br.com.unit.tokseg.armario_inteligente.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela geração, validação e manipulação de tokens JWT.
 * Implementa as operações necessárias para autenticação baseada em JWT.
 * 
 * Este serviço é responsável por:
 * - Gerar tokens JWT para usuários autenticados
 * - Validar tokens JWT
 * - Extrair informações dos tokens
 * - Gerenciar a expiração dos tokens
 */
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Extrai o nome de usuário (email) de um token JWT.
     * 
     * @param token Token JWT
     * @return Email do usuário
     */
    public String extractUsername(String token) {
        logger.debug("Extraindo nome de usuário do token");
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai uma claim específica de um token JWT.
     * 
     * @param token Token JWT
     * @param claimsResolver Função para extrair a claim desejada
     * @return Valor da claim extraída
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        logger.debug("Extraindo claim do token");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Gera um token JWT para um usuário.
     * 
     * @param userDetails Detalhes do usuário
     * @return Token JWT gerado
     */
    public String generateToken(UserDetails userDetails) {
        logger.info("Gerando token para usuário: {}", userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList()));
        return generateToken(claims, userDetails);
    }

    /**
     * Gera um token JWT para um usuário com claims extras.
     * 
     * @param extraClaims Claims adicionais a serem incluídas no token
     * @param userDetails Detalhes do usuário
     * @return Token JWT gerado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        logger.info("Gerando token com claims extras para usuário: {}", userDetails.getUsername());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Verifica se um token JWT é válido para um usuário.
     * 
     * @param token Token JWT
     * @param userDetails Detalhes do usuário
     * @return true se o token for válido, false caso contrário
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        logger.debug("Validando token para usuário: {}", userDetails.getUsername());
        final String username = extractUsername(token);
        boolean isValid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        if (!isValid) {
            logger.warn("Token inválido para usuário: {}", userDetails.getUsername());
        }
        return isValid;
    }

    /**
     * Verifica se um token JWT está expirado.
     * 
     * @param token Token JWT
     * @return true se o token estiver expirado, false caso contrário
     */
    private boolean isTokenExpired(String token) {
        boolean isExpired = extractExpiration(token).before(new Date());
        if (isExpired) {
            logger.warn("Token expirado");
        }
        return isExpired;
    }

    /**
     * Extrai a data de expiração de um token JWT.
     * 
     * @param token Token JWT
     * @return Data de expiração do token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai todas as claims de um token JWT.
     * 
     * @param token Token JWT
     * @return Claims do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtém a chave de assinatura para os tokens JWT.
     * 
     * @return Chave de assinatura
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}