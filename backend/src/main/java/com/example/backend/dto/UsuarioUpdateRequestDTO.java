package com.example.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateRequestDTO {

    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    private String nome;

    @Size(min = 3, message = "O nome de usuário deve ter no mínimo 3 caracteres.")
    private String nomeUsuario;
}