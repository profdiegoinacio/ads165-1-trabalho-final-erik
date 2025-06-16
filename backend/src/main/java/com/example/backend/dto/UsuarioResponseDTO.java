package com.example.backend.dto;

import com.example.backend.domain.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor; // Adicione este import

import java.util.Set;

@Data
@NoArgsConstructor // Adiciona um construtor sem argumentos (bom para JPA e frameworks)
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String nomeUsuario;
    private String email;
    private String telefone;
    private Set<String> roles;

    // Construtor que converte uma entidade Usuario para este DTO (Adicionado de volta)
    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.email = usuario.getEmail();
        this.telefone = usuario.getTelefone();
        this.roles = usuario.getRoles();
    }
}