package com.example.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A nota é obrigatória.")
    @Min(value = 1, message = "A nota mínima é 1.")
    @Max(value = 5, message = "A nota máxima é 5.")
    @Column(nullable = false)
    private Integer nota;

    @Column(length = 1000) // Limita o tamanho do comentário
    private String comentario;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;

    // --- RELACIONAMENTOS ---

    /**
     * O usuário que FEZ a avaliação.
     * Muitas avaliações podem ser feitas por um mesmo usuário.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliador_id", nullable = false)
    private Usuario avaliador;

    /**
     * O profissional que RECEBEU a avaliação.
     * Muitas avaliações podem ser para um mesmo profissional.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliado_id", nullable = false)
    private Usuario avaliado;


    // Método para definir a data de criação automaticamente antes de salvar
    @PrePersist
    protected void onCreate() {
        this.dataAvaliacao = LocalDateTime.now();
    }

    // Construtor padrão
    public Avaliacao() {}

    // Getters e Setters (Lombok ou manuais)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDateTime dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
    public Usuario getAvaliador() { return avaliador; }
    public void setAvaliador(Usuario avaliador) { this.avaliador = avaliador; }
    public Usuario getAvaliado() { return avaliado; }
    public void setAvaliado(Usuario avaliado) { this.avaliado = avaliado; }
}