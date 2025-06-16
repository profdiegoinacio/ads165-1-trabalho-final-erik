package com.example.backend.dto;

import lombok.Data;

import java.util.Set;

/**
 * DTO para enviar os dados completos de um perfil para o frontend.
 */
@Data
public class PerfilResponseDTO {
    // Dados do Usuário
    private Long id;
    private String nome;
    private String nomeUsuario;
    private String email; // Opcional, talvez queira mostrar no perfil
    private Set<String> roles;

    private boolean seguindoPeloUsuarioLogado;

    private Double notaMedia;
    private Long totalAvaliacoes;


    // Dados do Perfil
    private String bio;
    private String formacao;
    private String fotoPerfilUrl;
    private String fotoCapaUrl;

    // Construtor para facilitar a criação a partir da entidade Usuario

}