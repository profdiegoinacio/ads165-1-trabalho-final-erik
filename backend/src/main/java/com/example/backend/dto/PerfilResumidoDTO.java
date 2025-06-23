package com.example.backend.dto;

import com.example.backend.domain.Perfil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PerfilResumidoDTO {

    private String bio;
    private String formacao;
    private String fotoPerfilUrl;
    private boolean isProfissional;

    public PerfilResumidoDTO(Perfil perfil) {
        if (perfil != null) {
            this.bio = perfil.getBio();
            this.formacao = perfil.getFormacao();
            this.fotoPerfilUrl = perfil.getFotoPerfilUrl();
            this.isProfissional = perfil.isProfissional();
        }
    }
}
