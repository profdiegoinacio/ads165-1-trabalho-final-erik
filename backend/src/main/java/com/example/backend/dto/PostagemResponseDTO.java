package com.example.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para representar a resposta de uma postagem a ser enviada para o frontend.
 */
@Data // Anotação do Lombok para gerar Getters, Setters, toString, etc.
public class PostagemResponseDTO {

    private Long id;
    private String conteudo;
    private String urlMidia;
    private LocalDateTime dataCriacao;
    private AutorDTO autor; // Inclui um DTO aninhado para as informações do autor

    /**
     * DTO aninhado para representar o autor da postagem de forma simplificada.
     */
    @Data
    public static class AutorDTO {
        private Long id;
        private String nome;
        private String nomeUsuario;
        // Não inclua informações sensíveis como email, senha, etc.
    }
}