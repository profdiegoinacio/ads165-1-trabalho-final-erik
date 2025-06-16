package com.example.backend.repository.specification;

import com.example.backend.domain.Usuario;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {

    /**
     * Cria uma Specification para buscar usuários cujo nome OU nome de usuário
     * contenham o termo de busca, ignorando maiúsculas/minúsculas.
     *
     * @param query O termo a ser buscado.
     * @return Uma Specification para ser usada no repositório.
     */
    public static Specification<Usuario> comFiltroDeNome(String query) {
        // Se a query for nula ou vazia, retorna uma specification que não aplica filtros.
        if (query == null || query.trim().isEmpty()) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        // Retorna a lógica da query
        return (root, criteriaQuery, criteriaBuilder) -> {
            String queryFormatada = "%" + query.toLowerCase() + "%";

            // Cria a condição para buscar no campo 'nome'
            var predicadoNome = criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), queryFormatada);

            // Cria a condição para buscar no campo 'nomeUsuario'
            var predicadoUsername = criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeUsuario")), queryFormatada);

            // Retorna a combinação das duas condições com um "OR"
            return criteriaBuilder.or(predicadoNome, predicadoUsername);
        };
    }
}