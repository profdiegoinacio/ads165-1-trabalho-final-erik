package com.example.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para receber dados de atualização de perfil do frontend.
 * Os campos são opcionais, permitindo atualizações parciais.
 */
@Data
public class PerfilUpdateRequestDTO {

    @Size(max = 2000, message = "A biografia pode ter no máximo 2000 caracteres.")
    private String bio;

    @Size(max = 255, message = "A formação pode ter no máximo 255 caracteres.")
    private String formacao;

    @Size(max = 255, message = "A URL da foto de perfil pode ter no máximo 255 caracteres.")
    private String fotoPerfilUrl;

    @Size(max = 255, message = "A URL da foto de capa pode ter no máximo 255 caracteres.")
    private String fotoCapaUrl;
}