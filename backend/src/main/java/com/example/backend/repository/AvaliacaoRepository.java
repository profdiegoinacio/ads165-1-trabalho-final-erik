package com.example.backend.repository;

import com.example.backend.domain.Avaliacao;
import com.example.backend.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    Page<Avaliacao> findByAvaliadoOrderByDataAvaliacaoDesc(Usuario avaliado, Pageable pageable);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.avaliado = :avaliado")
    Double findAverageNotaByAvaliado(@Param("avaliado") Usuario avaliado);

    long countByAvaliado(Usuario avaliado);

    boolean existsByAvaliadorAndAvaliado(Usuario avaliador, Usuario avaliado);
}