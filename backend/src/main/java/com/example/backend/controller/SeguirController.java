package com.example.backend.controller;

import com.example.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/usuarios/{username}/seguir")
@CrossOrigin(origins = "*")
public class SeguirController {

    private static final Logger log = LoggerFactory.getLogger(SeguirController.class);
    private final UsuarioService usuarioService;

    @Autowired
    public SeguirController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Void> seguir(@PathVariable("username") String username, Authentication authentication) {
        if (authentication == null) {
            log.error("Endpoint /seguir atingido, mas o objeto Authentication está NULO.");
            return ResponseEntity.status(401).build();
        }

        log.info("Endpoint /seguir atingido por: '{}'. Usuário alvo: '{}'.", authentication.getName(), username);
        String usernameSeguidor = authentication.getName();
        usuarioService.seguirUsuario(usernameSeguidor, username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deixarDeSeguir(@PathVariable("username") String username, Authentication authentication) {
        if (authentication == null) {
            log.error("Endpoint /deixar-de-seguir atingido, mas o objeto Authentication está NULO.");
            return ResponseEntity.status(401).build();
        }

        log.info("Endpoint /deixar-de-seguir atingido por: '{}'. Usuário alvo: '{}'.", authentication.getName(), username);
        String usernameSeguidor = authentication.getName();
        usuarioService.deixarDeSeguirUsuario(usernameSeguidor, username);
        return ResponseEntity.ok().build();
    }
}
