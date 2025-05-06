package com.example.backend.controller;

import com.example.backend.domain.Usuario;
import com.example.backend.dto.UsuarioRegistroDTO;
import com.example.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    // Injeção de dependência via construtor
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody @Valid UsuarioRegistroDTO usuarioDTO) {
        log.info("Recebida requisição POST para /api/v1/usuarios/registrar com dados: {}", usuarioDTO);
        try {
            Usuario usuarioSalvo = usuarioService.registrarNovoUsuario(usuarioDTO);
            log.info("Controller: Usuário registrado com sucesso. ID: {}", usuarioSalvo.getId());


         URI location = ServletUriComponentsBuilder
                 .fromCurrentRequest().path("/{id}")
                 .buildAndExpand(usuarioSalvo.getId()).toUri();
         return ResponseEntity.created(location).body(usuarioSalvo);




        } catch (IllegalArgumentException e) {
            log.warn("Controller: Falha no registro - {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Controller: Erro inesperado no registro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao registrar usuário.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisição GET para /api/v1/usuarios/{}", id);
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);

        return usuarioOptional
                .map(usuario -> {
                    log.info("Usuário com ID {} encontrado.", id);
                    // Precisa melhorar a segurança de senhas expostas nessa parte
                    return ResponseEntity.ok(usuario);
                })
                .orElseGet(() -> {
                    log.warn("Usuário com ID {} não encontrado para GET.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        log.info("Recebida requisição GET para /api/v1/usuarios");
        List<Usuario> usuarios = usuarioService.listarTodos();
        if (usuarios.isEmpty()) {
            log.info("Nenhum usuário encontrado para listar.");
            return ResponseEntity.noContent().build();
        }
        // Precisa melhorar a segurança das senhas nessa parte tbm
        log.info("Retornando lista com {} usuários.", usuarios.size());
        return ResponseEntity.ok(usuarios);
    }

    //Ainda falta adicionar put e delete aqui
}