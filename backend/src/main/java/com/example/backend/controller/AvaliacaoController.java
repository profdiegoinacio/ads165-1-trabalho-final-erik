package com.example.backend.controller;

import com.example.backend.dto.AvaliacaoRequestDTO;
import com.example.backend.dto.AvaliacaoResponseDTO;
import com.example.backend.dto.PaginatedResponseDTO;
import com.example.backend.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/v1/usuarios/{username}/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @Autowired
    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<AvaliacaoResponseDTO>> listarAvaliacoes(
            @PathVariable String username,
            @PageableDefault(size = 5, sort = "dataAvaliacao", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AvaliacaoResponseDTO> paginaDeAvaliacoes = avaliacaoService.listarAvaliacoesPorUsuario(username, pageable);
        return ResponseEntity.ok(new PaginatedResponseDTO<>(paginaDeAvaliacoes));
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criarAvaliacao(
            @PathVariable String username,
            @RequestBody @Valid AvaliacaoRequestDTO dto,
            Authentication authentication) {


        String usernameAvaliador = authentication.getName();

        AvaliacaoResponseDTO avaliacaoCriada = avaliacaoService.criarAvaliacao(dto, usernameAvaliador, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoCriada);
    }
}