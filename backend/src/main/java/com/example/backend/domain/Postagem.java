package com.example.backend.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // Marca esta classe como uma entidade JPA
@Table(name = "postagens") // Define o nome da tabela no banco
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000) // Conteúdo do post, não pode ser nulo
    private String conteudo;

    // Opcional: para o caso de um post com imagem ou vídeo
    @Column(name = "url_midia")
    private String urlMidia;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    // --- Relacionamento Muitos-para-Um ---
    // Muitas postagens podem ser de um único usuário.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: só carrega o usuário quando for acessado
    @JoinColumn(name = "usuario_id", nullable = false) // Define a chave estrangeira na tabela 'postagens'
    private Usuario autor; // A entidade Usuario que criamos anteriormente

    // Construtor padrão exigido pelo JPA
    public Postagem() {
    }

    // Método que é executado antes de a entidade ser persistida pela primeira vez
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

    // Getters e Setters (pode usar Lombok se preferir)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getUrlMidia() {
        return urlMidia;
    }

    public void setUrlMidia(String urlMidia) {
        this.urlMidia = urlMidia;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }
}