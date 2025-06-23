package com.example.backend.repository;

import com.example.backend.domain.Postagem;
import com.example.backend.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    Page<Postagem> findAllByOrderByDataCriacaoDesc(Pageable pageable);

    Page<Postagem> findByAutor(Usuario autor, Pageable pageable);

}