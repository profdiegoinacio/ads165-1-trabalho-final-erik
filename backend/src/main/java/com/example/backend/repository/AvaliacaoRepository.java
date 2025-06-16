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

    /**
     * Busca todas as avaliações recebidas por um usuário específico (o 'avaliado'),
     * de forma paginada e ordenada da mais recente para a mais antiga.
     * @param avaliado O usuário que recebeu as avaliações.
     * @param pageable Objeto para paginação e ordenação.
     * @return Uma página de avaliações.
     */
    Page<Avaliacao> findByAvaliadoOrderByDataAvaliacaoDesc(Usuario avaliado, Pageable pageable);

    /**
     * Calcula a média das notas de todas as avaliações recebidas por um usuário.
     * Usa uma query JPQL customizada para fazer o cálculo diretamente no banco,
     * o que é muito mais eficiente do que buscar todas as notas.
     * @param avaliado O usuário que recebeu as avaliações.
     * @return Um Double contendo a nota média, ou null se não houver avaliações.
     */
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.avaliado = :avaliado")
    Double findAverageNotaByAvaliado(@Param("avaliado") Usuario avaliado);

    /**
     * Conta o número total de avaliações recebidas por um usuário.
     * É mais eficiente do que buscar todos os registros para depois contar.
     * @param avaliado O usuário que recebeu as avaliações.
     * @return O número total de avaliações.
     */
    long countByAvaliado(Usuario avaliado);

    /**
     * Verifica se um usuário (avaliador) já avaliou outro usuário (avaliado).
     * Útil para implementar a regra de negócio de que um usuário só pode avaliar outro uma vez.
     * @param avaliador O usuário que está fazendo a avaliação.
     * @param avaliado O usuário que está sendo avaliado.
     * @return true se já existe uma avaliação, false caso contrário.
     */
    boolean existsByAvaliadorAndAvaliado(Usuario avaliador, Usuario avaliado);
}