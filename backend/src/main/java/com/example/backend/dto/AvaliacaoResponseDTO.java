package com.example.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvaliacaoResponseDTO {
    private Long id;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
    private AutorDTO avaliador; // Quem fez a avaliação

    // DTO aninhado para informações do autor da avaliação
    @Data
    public static class AutorDTO {
        private Long id;
        private String nome;
        private String nomeUsuario;
        // private String fotoPerfilUrl; // Poderíamos adicionar no futuro
    }
}