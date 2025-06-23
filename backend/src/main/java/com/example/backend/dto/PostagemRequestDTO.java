package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostagemRequestDTO {

    @NotBlank(message = "O conteúdo não pode estar em branco.")
    @Size(max = 2000, message = "O conteúdo pode ter no máximo 2000 caracteres.")
    private String conteudo;

    private String urlMidia;
}