package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "Login (nome de usuário) não pode estar em branco")
    private String login;

    @NotBlank(message = "Senha não pode estar em branco")
    private String senha;
}