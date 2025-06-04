package com.example.backend.controller;

import com.example.backend.domain.Usuario;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.TokenDTO;
import com.example.backend.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AutenticacaoController {

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoController.class);

    @Autowired
    private AuthenticationManager authenticationManager; // Gerenciador de autenticação do Spring

    @Autowired
    private TokenService tokenService; // Nosso serviço de token

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) { // Mude o tipo de retorno
        log.info("Tentativa de login para usuário: {}", loginDTO.getLogin());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getSenha());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
            String tokenJWT = tokenService.gerarToken(usuarioAutenticado);

            List<String> roles = usuarioAutenticado.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            log.info("Login bem-sucedido para usuário: {}", loginDTO.getLogin());
            return ResponseEntity.ok(new AuthResponseDTO(
                    tokenJWT,
                    usuarioAutenticado.getUsername(), // ou getNomeUsuario()
                    roles
                    // , usuarioAutenticado.getId(), // Se adicionar id ao DTO
                    // , usuarioAutenticado.getEmail() // Se adicionar email ao DTO
            ));

        } catch (Exception e) {
            log.warn("Falha na autenticação para usuário: {}. Motivo: {}", loginDTO.getLogin(), e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }
}