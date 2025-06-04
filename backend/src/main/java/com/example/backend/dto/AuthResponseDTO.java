package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String username; // Ou nomeUsuario
    private List<String> roles;
    // Adicione outros campos do usuário que o frontend possa precisar, como 'id' ou 'email'
    // private Long id;
    // private String email;
}