package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UsuarioRegistroDTO {

    @NotBlank(message = "Nome não pode estar em branco")
    private String nome;

    @NotBlank(message = "Email não pode estar em branco")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "Nome de usuário não pode estar em branco")
    @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
    private String nomeUsuario;

    // Validação de telefone pode ser mais específica (regex) ou opcional
    private String telefone;

    @NotBlank(message = "Senha não pode estar em branco")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    private Set<String> roles;
}
