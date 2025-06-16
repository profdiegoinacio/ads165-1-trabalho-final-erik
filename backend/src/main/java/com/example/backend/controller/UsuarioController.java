package com.example.backend.controller;

import com.example.backend.domain.Usuario;
import com.example.backend.dto.PerfilResponseDTO;
import com.example.backend.dto.UsuarioRegistroDTO;
import com.example.backend.dto.UsuarioResponseDTO;
import com.example.backend.dto.UsuarioUpdateRequestDTO;
import com.example.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody @Valid UsuarioRegistroDTO usuarioDTO) {
        try {
            Usuario usuarioSalvo = usuarioService.registrarNovoUsuario(usuarioDTO);
            log.info("Controller: Usuário registrado com sucesso. ID: {}", usuarioSalvo.getId());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(usuarioSalvo.getId()).toUri();

            // Retorna o DTO seguro em vez da entidade completa
            return ResponseEntity.created(location).body(new UsuarioResponseDTO(usuarioSalvo));

        } catch (IllegalArgumentException e) {
            log.warn("Controller: Falha no registro - {}", e.getMessage());
            // Para uma API REST, é melhor retornar uma resposta com o erro no corpo
            return ResponseEntity.badRequest().body(null); // O GlobalExceptionHandler cuidaria disso melhor
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        log.info("Recebida requisição GET para /api/v1/usuarios");
        List<Usuario> usuarios = usuarioService.listarTodos();

        List<UsuarioResponseDTO> dtos = usuarios.stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());

        if (dtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<UsuarioResponseDTO>> buscarUsuarios(
            @RequestParam(value = "query", required = false) String query,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuarios(query, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisição GET para /api/v1/usuarios/{}", id);
        return usuarioService.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(new UsuarioResponseDTO(usuario)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{username}/perfil")
    public ResponseEntity<PerfilResponseDTO> getPerfilDoUsuario(
            @PathVariable("username") String username,
            Authentication authentication) {

        String usuarioLogadoUsername = (authentication != null) ? authentication.getName() : null;
        PerfilResponseDTO perfilDTO = usuarioService.buscarPerfilPorUsername(username, usuarioLogadoUsername);
        return ResponseEntity.ok(perfilDTO);
    }

    @PatchMapping("/me/dados-principais")
    public ResponseEntity<PerfilResponseDTO> atualizarDadosPrincipais(
            @RequestBody @Valid UsuarioUpdateRequestDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        PerfilResponseDTO perfilAtualizado = usuarioService.atualizarDadosPrincipais(username, dto);
        return ResponseEntity.ok(perfilAtualizado);
    }
}