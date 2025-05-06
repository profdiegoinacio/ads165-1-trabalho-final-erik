package com.example.backend.controller;

import com.example.backend.domain.Usuario;
import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.TokenDTO;
import com.example.backend.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/auth")
public class AutenticacaoController {

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoController.class);

    @Autowired
    private AuthenticationManager authenticationManager; // Gerenciador de autenticação do Spring

    @Autowired
    private TokenService tokenService; // Nosso serviço de token

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("Tentativa de login para usuário: {}", loginDTO.getLogin());
        try {
            //Cria um objeto de autenticação com as credenciais recebidas
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getSenha());

            //Tenta autenticar o usuário
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            //Se a autenticação for bem-sucedida, gera o token JWT
            Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal(); // Pega o usuário autenticado
            String tokenJWT = tokenService.gerarToken(usuarioAutenticado);

            log.info("Login bem-sucedido para usuário: {}", loginDTO.getLogin());
            return ResponseEntity.ok(new TokenDTO(tokenJWT, "Bearer"));

        } catch (Exception e) {
            log.warn("Falha na autenticação para usuário: {}. Motivo: {}", loginDTO.getLogin(), e.getMessage());
            // Em caso de falha na autenticação, o AuthenticationManager lança exceções
            return ResponseEntity.status(401).build(); // Retorna 401 Unauthorized
        }
    }
}