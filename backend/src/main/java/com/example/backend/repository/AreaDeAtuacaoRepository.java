package com.example.backend.repository;

import com.example.backend.domain.AreaDeAtuacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaDeAtuacaoRepository extends JpaRepository<AreaDeAtuacao, Long> {
}