package com.example.backend.controller;

import com.example.backend.dto.PerfilUpdateRequestDTO;
import com.example.backend.dto.PerfilResponseDTO;
import com.example.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfis")
public class PerfilController {

    private final UsuarioService usuarioService;

    @Autowired
    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para o usuário autenticado atualizar seu próprio perfil.
     *
     * @param dto Os dados a serem atualizados, vindos do corpo da requisição.
     * @param authentication Dados do usuário autenticado, injetados pelo Spring Security.
     * @return O perfil com os dados atualizados.
     */
    @PutMapping("/me") // "me" é uma convenção REST para se referir ao recurso do usuário logado
    public ResponseEntity<PerfilResponseDTO> atualizarMeuPerfil(
            @RequestBody @Valid PerfilUpdateRequestDTO dto,
            Authentication authentication) {

        // Pega o nome de usuário do principal autenticado para garantir a segurança
        String username = authentication.getName();

        PerfilResponseDTO perfilAtualizado = usuarioService.atualizarPerfil(username, dto);

        return ResponseEntity.ok(perfilAtualizado);
    }
}