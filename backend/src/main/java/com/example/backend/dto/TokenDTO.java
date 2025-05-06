package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // Construtor para facilitar a criação
public class TokenDTO {
    private String token;
    private String tipo;
}