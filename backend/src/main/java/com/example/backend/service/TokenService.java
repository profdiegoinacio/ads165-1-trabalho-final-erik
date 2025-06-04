package com.example.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.backend.domain.Usuario; // Importe sua classe Usuario
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    // Injeta os valores do application.properties
    @Value("${api.security.jwt.secret}")
    private String secret;

    @Value("${api.security.jwt.expiration-ms}")
    private long expirationMs;

    private static final String ISSUER = "API ConectaPro"; // Emissor do token

    public String gerarToken(Usuario usuario) { // Recebe Usuario diretamente
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Coleta os papéis do usuário
            List<String> rolesList = usuario.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getNomeUsuario()) // username
                    .withClaim("roles", rolesList)         // Adiciona a claim de papéis
                    // .withClaim("userId", usuario.getId()) // Exemplo de outra claim útil
                    .withExpiresAt(calcularDataExpiracao())
                    .sign(algorithm);
            log.debug("Token gerado para usuário: {} com papéis: {}", usuario.getNomeUsuario(), rolesList);
            return token;
        } catch (JWTCreationException exception){
            log.error("Erro ao gerar token JWT para usuário: {}", usuario.getNomeUsuario(), exception);
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER) // Verifica se o emissor é o esperado
                    .build()
                    .verify(tokenJWT) // Verifica a assinatura e validade
                    .getSubject(); // Retorna o subject (nomeUsuario)
        } catch (JWTVerificationException exception){
            log.warn("Token JWT inválido ou expirado: {}", exception.getMessage());
            // Retornar null ou lançar exceção dependendo de como o filtro tratará
            return null;
        }
    }

    private Instant calcularDataExpiracao() {
        return LocalDateTime.now().plusNanos(expirationMs * 1_000_000L) // Convertendo ms para nanos
                .toInstant(ZoneOffset.of("-03:00")); // Ajuste para o fuso horário de Brasília
    }
}