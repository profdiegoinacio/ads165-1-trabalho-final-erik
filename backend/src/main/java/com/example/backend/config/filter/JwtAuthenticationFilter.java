package com.example.backend.config.filter;

import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component // Marca como um componente Spring para ser gerenciado
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Garante execução única por requisição

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository; // Para buscar o usuário pelo nome

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            log.debug("Token JWT encontrado na requisição: {}", request.getRequestURI());
            String subject = tokenService.getSubject(tokenJWT); // Valida o token e pega o subject (username)

            if (subject != null) {
                log.debug("Subject extraído do token: {}", subject);
                // Busca o usuário no banco de dados
                UserDetails usuario = usuarioRepository.findByNomeUsuario(subject).orElse(null);

                if (usuario != null) {
                    log.debug("Usuário encontrado no banco: {}", subject);
                    // Cria o objeto Authentication para o Spring Security
                    // Passamos null como credenciais pois a autenticação já foi feita pelo token
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                    // Define o usuário como autenticado no contexto de segurança do Spring
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Usuário autenticado via JWT e definido no SecurityContext");
                } else {
                    log.warn("Usuário referente ao subject '{}' do token não encontrado no banco.", subject);
                }
            } else {
                log.debug("Token JWT inválido ou expirado para requisição: {}", request.getRequestURI());
            }
        } else {
            log.trace("Nenhum token JWT encontrado no cabeçalho Authorization para: {}", request.getRequestURI());
        }

        // Continua a cadeia de filtros (permite que a requisição prossiga)
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove o prefixo "Bearer "
        }
        return null; // Retorna null se não encontrar o token no formato esperado
    }
}