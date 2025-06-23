package com.example.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "perfis")
public class Perfil {

    @Id
    private Long id;

    private String bio;

    @Column(name = "formacao")
    private String formacao;

    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;

    @Column(name = "foto_capa_url")
    private String fotoCapaUrl;

    @Column(name = "is_profissional", nullable = false, columnDefinition = "boolean default false")
    private boolean isProfissional = false;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getFotoCapaUrl() {
        return fotoCapaUrl;
    }

    public void setFotoCapaUrl(String fotoCapaUrl) {
        this.fotoCapaUrl = fotoCapaUrl;
    }

    public boolean isProfissional() {
        return isProfissional;
    }

    public void setProfissional(boolean profissional) {
        isProfissional = profissional;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
