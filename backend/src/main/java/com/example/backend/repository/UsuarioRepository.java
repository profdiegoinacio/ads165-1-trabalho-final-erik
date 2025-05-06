package com.example.backend.repository;

import com.example.backend.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Busc usuario por nome
    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    //Busca por email
    Optional<Usuario> findByEmail(String email); // Mantido por ser útil

//verifica se o email ja existe no banco de dados
    boolean existsByEmail(String email);

//verifica se o nome de usuario ja existe no banco de dados
    boolean existsByNomeUsuario(String nomeUsuario);

}