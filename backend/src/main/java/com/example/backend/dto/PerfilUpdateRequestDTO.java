package com.example.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PerfilUpdateRequestDTO {

    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    private String nome;

    @Size(max = 2000, message = "A biografia pode ter no máximo 2000 caracteres.")
    private String bio;

    @Size(max = 255, message = "A formação pode ter no máximo 255 caracteres.")
    private String formacao;

    @Size(max = 255, message = "A URL da foto de perfil pode ter no máximo 255 caracteres.")
    private String fotoPerfilUrl;

    @Size(max = 255, message = "A URL da foto de capa pode ter no máximo 255 caracteres.")
    private String fotoCapaUrl;

    private Boolean isProfissional;

    private Set<Long> areasDeAtuacaoIds;

    @Size(min = 3, message = "O nome de usuário deve ter no mínimo 3 caracteres.")
    private String nomeUsuario;
}