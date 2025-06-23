package com.example.backend.repository.specification;

import com.example.backend.domain.AreaDeAtuacao;
import com.example.backend.domain.Usuario;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;


public class UsuarioSpecification {

    public static Specification<Usuario> comFiltros(String nomeQuery, Long areaId) {
        return Specification
                .where(comNome(nomeQuery))
                .and(daAreaDeAtuacao(areaId));
    }

    private static Specification<Usuario> comNome(String nomeQuery) {
        if (nomeQuery == null || nomeQuery.trim().isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            String queryFormatada = "%" + nomeQuery.toLowerCase() + "%";
            var predicadoNome = criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), queryFormatada);
            var predicadoUsername = criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeUsuario")), queryFormatada);
            return criteriaBuilder.or(predicadoNome, predicadoUsername);
        };
    }

    private static Specification<Usuario> daAreaDeAtuacao(Long areaId) {
        if (areaId == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Join<Usuario, AreaDeAtuacao> joinAreas = root.join("areasDeAtuacao");
            return criteriaBuilder.equal(joinAreas.get("id"), areaId);
        };
    }
}