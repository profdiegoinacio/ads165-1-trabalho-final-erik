package com.example.backend.service;

import com.example.backend.domain.Avaliacao;
import com.example.backend.domain.Usuario;
import com.example.backend.dto.AvaliacaoRequestDTO;
import com.example.backend.dto.AvaliacaoResponseDTO;
import com.example.backend.repository.AvaliacaoRepository;
import com.example.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, UsuarioRepository usuarioRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cria uma nova avaliação, aplicando regras de negócio.
     */
    @Transactional
    public AvaliacaoResponseDTO criarAvaliacao(AvaliacaoRequestDTO dto, String usernameAvaliador, String usernameAvaliado) {
        // Regra 1: Usuário não pode avaliar a si mesmo.
        if (usernameAvaliador.equalsIgnoreCase(usernameAvaliado)) {
            throw new IllegalArgumentException("Você não pode avaliar a si mesmo.");
        }

        // Busca os usuários envolvidos no banco de dados.
        Usuario avaliador = usuarioRepository.findByNomeUsuario(usernameAvaliador)
                .orElseThrow(() -> new EntityNotFoundException("Usuário avaliador não encontrado: " + usernameAvaliador));

        Usuario avaliado = usuarioRepository.findByNomeUsuario(usernameAvaliado)
                .orElseThrow(() -> new EntityNotFoundException("Usuário a ser avaliado não encontrado: " + usernameAvaliado));

        // Regra 2: Usuário só pode avaliar outro usuário uma única vez.
        if (avaliacaoRepository.existsByAvaliadorAndAvaliado(avaliador, avaliado)) {
            throw new IllegalArgumentException("Você já avaliou este profissional.");
        }

        // Se todas as regras passarem, cria e salva a nova avaliação.
        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setNota(dto.getNota());
        novaAvaliacao.setComentario(dto.getComentario());
        novaAvaliacao.setAvaliador(avaliador);
        novaAvaliacao.setAvaliado(avaliado);
        // A data é definida pelo @PrePersist na entidade.

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(novaAvaliacao);

        return toResponseDTO(avaliacaoSalva);
    }

    /**
     * Lista todas as avaliações recebidas por um usuário.
     */
    @Transactional(readOnly = true)
    public Page<AvaliacaoResponseDTO> listarAvaliacoesPorUsuario(String username, Pageable pageable) {
        Usuario avaliado = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + username));

        Page<Avaliacao> avaliacoes = avaliacaoRepository.findByAvaliadoOrderByDataAvaliacaoDesc(avaliado, pageable);

        return avaliacoes.map(this::toResponseDTO);
    }

    // Método privado para converter a entidade Avaliacao para o DTO de resposta.
    private AvaliacaoResponseDTO toResponseDTO(Avaliacao avaliacao) {
        AvaliacaoResponseDTO dto = new AvaliacaoResponseDTO();
        dto.setId(avaliacao.getId());
        dto.setNota(avaliacao.getNota());
        dto.setComentario(avaliacao.getComentario());
        dto.setDataAvaliacao(avaliacao.getDataAvaliacao());

        AvaliacaoResponseDTO.AutorDTO autorDTO = new AvaliacaoResponseDTO.AutorDTO();
        autorDTO.setId(avaliacao.getAvaliador().getId());
        autorDTO.setNome(avaliacao.getAvaliador().getNome());
        autorDTO.setNomeUsuario(avaliacao.getAvaliador().getNomeUsuario());

        dto.setAvaliador(autorDTO);
        return dto;
    }

    /**
     * Busca a nota média de um usuário.
     * @param usuario O usuário avaliado.
     * @return A nota média como um Double, ou 0.0 se não houver avaliações.
     */
    public Double getNotaMediaPorUsuario(Usuario usuario) {
        Double media = avaliacaoRepository.findAverageNotaByAvaliado(usuario);
        return media == null ? 0.0 : media;
    }

    /**
     * Busca o número total de avaliações de um usuário.
     * @param usuario O usuário avaliado.
     * @return O total de avaliações como um Long.
     */
    public long getTotalAvaliacoesPorUsuario(Usuario usuario) {
        return avaliacaoRepository.countByAvaliado(usuario);
    }
}