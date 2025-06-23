package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO dto) {
        usuarioService.registrarNovoUsuario(dto);
        return ResponseEntity.ok("Usuário registrado com sucesso! Faça o login para continuar.");
    }
    @GetMapping("/{nomeUsuario}/perfil")
    public ResponseEntity<PerfilResponseDTO> buscarPerfil(@PathVariable String nomeUsuario, @AuthenticationPrincipal UserDetails userDetails) {
        String usuarioLogadoUsername = userDetails != null ? userDetails.getUsername() : null;
        PerfilResponseDTO perfilDTO = usuarioService.buscarPerfilPorUsername(nomeUsuario, usuarioLogadoUsername);
        return ResponseEntity.ok(perfilDTO);
    }
    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponseDTO> atualizarPerfil(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PerfilUpdateRequestDTO dto) {
        String username = userDetails.getUsername();
        PerfilResponseDTO perfilAtualizadoDTO = usuarioService.atualizarPerfilCompleto(username, dto);
        return ResponseEntity.ok(perfilAtualizadoDTO);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<UsuarioResponseDTO>> buscarUsuarios(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long areaId, // Recebe o ID da área como parâmetro
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuarios(query, areaId, pageable);
        return ResponseEntity.ok(usuarios);
    }
    @GetMapping("/profissionais")
    public ResponseEntity<Page<UsuarioResponseDTO>> buscarProfissionais(@PageableDefault(size = 10) Pageable pageable) {
        Page<UsuarioResponseDTO> profissionais = usuarioService.buscarProfissionais(pageable);
        return ResponseEntity.ok(profissionais);
    }
    @PostMapping("/{usernameParaSeguir}/seguir")
    public ResponseEntity<Void> seguirUsuario(@PathVariable String usernameParaSeguir, @AuthenticationPrincipal UserDetails userDetails) {
        String usernameSeguidor = userDetails.getUsername();
        usuarioService.seguirUsuario(usernameSeguidor, usernameParaSeguir);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{usernameParaDeixarDeSeguir}/deixar-de-seguir")
    public ResponseEntity<Void> deixarDeSeguirUsuario(@PathVariable String usernameParaDeixarDeSeguir, @AuthenticationPrincipal UserDetails userDetails) {
        String usernameSeguidor = userDetails.getUsername();
        usuarioService.deixarDeSeguirUsuario(usernameSeguidor, usernameParaDeixarDeSeguir);
        return ResponseEntity.ok().build();
    }
}