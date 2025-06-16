package com.example.backend.repository;

import com.example.backend.domain.Postagem;
import com.example.backend.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para acessar os dados da entidade Postagem no banco de dados.
 */
@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    // --- Métodos de Consulta Personalizados ---

    /**
     * Busca todas as postagens de forma paginada, ordenadas pela data de criação
     * do mais novo para o mais antigo.
     * Ideal para o feed principal da aplicação.
     *
     * @param pageable Objeto que contém informações de paginação e ordenação.
     * @return Uma página (Page) de postagens.
     */
    Page<Postagem> findAllByOrderByDataCriacaoDesc(Pageable pageable);


    /**
     * Busca todas as postagens de um autor específico, de forma paginada.
     * Ideal para a página de perfil de um usuário.
     *
     * @param autor O objeto Usuario que é o autor das postagens.
     * @param pageable Objeto que contém informações de paginação e ordenação.
     * @return Uma página (Page) de postagens do autor especificado.
     */
    Page<Postagem> findByAutor(Usuario autor, Pageable pageable);

}