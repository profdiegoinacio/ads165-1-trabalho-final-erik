package com.example.backend.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference; // Importante para evitar loop infinito

@Entity
@Table(name = "perfis")
public class Perfil {

    @Id
    private Long id; // Usaremos o mesmo ID do usuário

    private String bio;

    @Column(name = "formacao")
    private String formacao; // Ex: "Designer Gráfico formado na UPF"

    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;

    @Column(name = "foto_capa_url")
    private String fotoCapaUrl;

    // --- Relacionamento Um-para-Um ---
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Mapeia o ID desta entidade para ser o mesmo da entidade Usuario
    @JoinColumn(name = "id") // A coluna de junção é o próprio ID
    @JsonBackReference // Evita que o Perfil serialize o Usuário de volta, causando um loop
    private Usuario usuario;

    // Getters e Setters (pode usar Lombok)
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}