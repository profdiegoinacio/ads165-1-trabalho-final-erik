package com.example.backend.service;

import com.example.backend.domain.Postagem;
import com.example.backend.domain.Usuario;
import com.example.backend.dto.PostagemRequestDTO;
import com.example.backend.dto.PostagemResponseDTO;
import com.example.backend.repository.PostagemRepository;
import com.example.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostagemService {

    private final PostagemRepository postagemRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PostagemService(PostagemRepository postagemRepository, UsuarioRepository usuarioRepository) {
        this.postagemRepository = postagemRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PostagemResponseDTO criarPostagem(PostagemRequestDTO dto, String username) {
        Usuario autor = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário autor não encontrado: " + username));

        Postagem novaPostagem = new Postagem();
        novaPostagem.setConteudo(dto.getConteudo());
        novaPostagem.setUrlMidia(dto.getUrlMidia());
        novaPostagem.setAutor(autor);

        Postagem postagemSalva = postagemRepository.save(novaPostagem);

        return new PostagemResponseDTO(postagemSalva);
    }

    @Transactional(readOnly = true)
    public Page<PostagemResponseDTO> listarPostagens(Pageable pageable) {
        Page<Postagem> postagens = postagemRepository.findAllByOrderByDataCriacaoDesc(pageable);

        return postagens.map(PostagemResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<PostagemResponseDTO> listarPostagensPorUsuario(String username, Pageable pageable) {
        Usuario autor = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + username));

        Page<Postagem> postagens = postagemRepository.findByAutor(autor, pageable);

        return postagens.map(PostagemResponseDTO::new);
    }
}
