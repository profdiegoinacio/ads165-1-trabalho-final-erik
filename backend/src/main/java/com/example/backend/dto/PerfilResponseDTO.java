package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerfilResponseDTO {
    private Long id;
    private String nome;
    private String nomeUsuario;
    private String email;
    private String bio;
    private String formacao;
    private String fotoPerfilUrl;
    private String fotoCapaUrl;
    private boolean isProfissional;
    private Double notaMedia;
    private long totalAvaliacoes;
    private int totalSeguidores;
    private int totalSeguindo;
    private boolean seguindoPeloUsuarioLogado;
    private Set<String> roles;
    private List<AreaDeAtuacaoResponseDTO> areasDeAtuacao;
}