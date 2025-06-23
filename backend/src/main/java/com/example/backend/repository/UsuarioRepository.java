package com.example.backend.repository;

import com.example.backend.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    boolean existsByEmail(String email);

    boolean existsByNomeUsuario(String nomeUsuario);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.seguindo WHERE u.nomeUsuario = :username")
    Optional<Usuario> findByNomeUsuarioWithSeguindo(@Param("username") String username);

    @Query("SELECT size(u.seguidores) FROM Usuario u WHERE u.id = :id")
    int countSeguidoresById(@Param("id") Long id);

    @Query("SELECT size(u.seguindo) FROM Usuario u WHERE u.id = :id")
    int countSeguindoById(@Param("id") Long id);

    Page<Usuario> findByPerfilIsProfissional(boolean isProfissional, Pageable pageable);
}
