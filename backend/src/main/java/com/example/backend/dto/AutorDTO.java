package com.example.backend.dto;

import com.example.backend.domain.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AutorDTO {

    private String nome;
    private String nomeUsuario;
    private String fotoPerfilUrl;
    public AutorDTO(Usuario autor) {
        this.nome = autor.getNome();
        this.nomeUsuario = autor.getNomeUsuario();
        if (autor.getPerfil() != null) {
            this.fotoPerfilUrl = autor.getPerfil().getFotoPerfilUrl();
        }
    }
}
