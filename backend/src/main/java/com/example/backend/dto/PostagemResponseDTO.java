package com.example.backend.dto;

import com.example.backend.domain.Postagem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostagemResponseDTO {

    private Long id;
    private String conteudo;
    private String urlMidia;
    private LocalDateTime dataCriacao;
    private AutorDTO autor;

    public PostagemResponseDTO(Postagem postagem) {
        this.id = postagem.getId();
        this.conteudo = postagem.getConteudo();
        this.urlMidia = postagem.getUrlMidia();
        this.dataCriacao = postagem.getDataCriacao();

        this.autor = new AutorDTO(postagem.getAutor());
    }
}
