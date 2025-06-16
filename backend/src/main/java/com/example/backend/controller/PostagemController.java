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
import com.example.backend.dto.PaginatedResponseDTO; // Importe
import org.springframework.data.domain.Page; // Importe

@RestController
@RequestMapping("/api/v1/postagens") // Define o path base para todos os endpoints deste controller
public class PostagemController {

    private final PostagemService postagemService;

    @Autowired
    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    /**
     * Endpoint para listar todas as postagens de forma paginada (feed).
     * Este endpoint é público.
     *
     * @param pageable Parâmetros de paginação e ordenação injetados pelo Spring
     * Ex: /api/v1/postagens?page=0&size=10&sort=dataCriacao,desc
     * @return Uma página de postagens.
     */
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<PostagemResponseDTO>> listarPostagens( // Mude o tipo de retorno
                                                                                      @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostagemResponseDTO> paginaDePostagens = postagemService.listarPostagens(pageable);
        // Converte a página do Spring para o nosso DTO de resposta paginada
        return ResponseEntity.ok(new PaginatedResponseDTO<>(paginaDePostagens));
    }

    /**
     * Endpoint para buscar todas as postagens de um usuário específico.
     * Será usado na página de perfil.
     *
     * @param username O nome do usuário vindo da URL.
     * @param pageable Parâmetros de paginação.
     * @return Uma página de postagens do usuário.
     */
    @GetMapping("/usuario/{username}")
    public ResponseEntity<Page<PostagemResponseDTO>> getPostagensPorUsuario(
            @PathVariable String username,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostagemResponseDTO> postagens = postagemService.listarPostagensPorUsuario(username, pageable);
        return ResponseEntity.ok(postagens);
    }

    /**
     * Endpoint para criar uma nova postagem.
     * Este endpoint requer autenticação.
     *
     * @param dto Os dados da nova postagem vindos do corpo da requisição.
     * @param authentication Objeto injetado pelo Spring Security contendo os dados do usuário logado.
     * @return O DTO da postagem recém-criada com status 201 Created.
     */
    @PostMapping
    public ResponseEntity<PostagemResponseDTO> criarPostagem(
            @RequestBody @Valid PostagemRequestDTO dto,
            Authentication authentication) {

        // O nome de usuário é obtido do objeto Authentication, que é preenchido pelo nosso filtro JWT.
        String username = authentication.getName();

        PostagemResponseDTO postagemCriada = postagemService.criarPostagem(dto, username);

        // Retorna a resposta com status 201 e o corpo da postagem criada.
        // Poderíamos também retornar um header 'Location' com a URL do novo recurso.
        return ResponseEntity.status(HttpStatus.CREATED).body(postagemCriada);
    }

    // TODO: Adicionar endpoints para buscar postagens de um usuário específico, deletar, etc.
}