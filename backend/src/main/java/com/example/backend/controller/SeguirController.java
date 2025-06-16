package com.example.backend.controller;

import com.example.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios/{username}/seguir")
public class SeguirController {

    private final UsuarioService usuarioService;

    @Autowired
    public SeguirController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para seguir um usuário. Requer autenticação.
     * @param username O usuário a ser seguido (da URL).
     * @param authentication O usuário logado (o seguidor).
     * @return Resposta de sucesso sem conteúdo.
     */
    @PostMapping
    public ResponseEntity<Void> seguir(@PathVariable String username, Authentication authentication) {
        String usernameSeguidor = authentication.getName();
        usuarioService.seguirUsuario(usernameSeguidor, username);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para deixar de seguir um usuário. Requer autenticação.
     */
    @DeleteMapping
    public ResponseEntity<Void> deixarDeSeguir(@PathVariable String username, Authentication authentication) {
        String usernameSeguidor = authentication.getName();
        usuarioService.deixarDeSeguirUsuario(usernameSeguidor, username);
        return ResponseEntity.ok().build();
    }
}