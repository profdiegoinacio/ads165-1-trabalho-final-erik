package com.example.backend.controller;

import com.example.backend.dto.PostagemRequestDTO;
import com.example.backend.dto.PostagemResponseDTO;
import com.example.backend.service.PostagemService;
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
import com.example.backend.dto.PaginatedResponseDTO;

@RestController
@RequestMapping("/api/v1/postagens")
public class PostagemController {

    private final PostagemService postagemService;

    @Autowired
    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<PostagemResponseDTO>> listarPostagens( // Mude o tipo de retorno
                                                                                      @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostagemResponseDTO> paginaDePostagens = postagemService.listarPostagens(pageable);
        // Converte a página do Spring para o nosso DTO de resposta paginada
        return ResponseEntity.ok(new PaginatedResponseDTO<>(paginaDePostagens));
    }

    @GetMapping("/usuario/{username}")
    public ResponseEntity<Page<PostagemResponseDTO>> getPostagensPorUsuario(
            @PathVariable String username,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostagemResponseDTO> postagens = postagemService.listarPostagensPorUsuario(username, pageable);
        return ResponseEntity.ok(postagens);
    }

    @PostMapping
    public ResponseEntity<PostagemResponseDTO> criarPostagem(
            @RequestBody @Valid PostagemRequestDTO dto,
            Authentication authentication) {

        String username = authentication.getName();

        PostagemResponseDTO postagemCriada = postagemService.criarPostagem(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(postagemCriada);
    }
}