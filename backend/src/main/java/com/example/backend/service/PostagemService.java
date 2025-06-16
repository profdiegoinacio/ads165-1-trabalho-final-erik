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

    /**
     * Cria uma nova postagem associada ao usuário autenticado.
     *
     * @param dto Os dados da nova postagem.
     * @param username O nome de usuário do autor (obtido do token JWT).
     * @return O DTO da postagem criada.
     */
    @Transactional
    public PostagemResponseDTO criarPostagem(PostagemRequestDTO dto, String username) {
        // 1. Encontra o usuário autor no banco de dados
        Usuario autor = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário autor não encontrado: " + username));

        // 2. Cria a nova entidade Postagem
        Postagem novaPostagem = new Postagem();
        novaPostagem.setConteudo(dto.getConteudo());
        novaPostagem.setUrlMidia(dto.getUrlMidia());
        novaPostagem.setAutor(autor);
        // A data de criação é definida automaticamente pelo @PrePersist na entidade

        // 3. Salva a postagem no banco
        Postagem postagemSalva = postagemRepository.save(novaPostagem);

        // 4. Converte a entidade salva para o DTO de resposta e retorna
        return toResponseDTO(postagemSalva);
    }

    /**
     * Lista todas as postagens de forma paginada para o feed principal.
     *
     * @param pageable Configurações de paginação e ordenação.
     * @return Uma página de DTOs de postagem.
     */
    @Transactional(readOnly = true)
    public Page<PostagemResponseDTO> listarPostagens(Pageable pageable) {
        // Busca a página de entidades Postagem do repositório
        Page<Postagem> postagens = postagemRepository.findAllByOrderByDataCriacaoDesc(pageable);
        // Converte a página de entidades para uma página de DTOs
        return postagens.map(this::toResponseDTO);
    }

    // --- Métodos Privados Auxiliares ---

    /**
     * Converte uma entidade Postagem para seu DTO de resposta correspondente.
     *
     * @param postagem A entidade a ser convertida.
     * @return O DTO de resposta.
     */
    private PostagemResponseDTO toResponseDTO(Postagem postagem) {
        PostagemResponseDTO dto = new PostagemResponseDTO();
        PostagemResponseDTO.AutorDTO autorDTO = new PostagemResponseDTO.AutorDTO();

        // Mapeia os dados do autor
        autorDTO.setId(postagem.getAutor().getId());
        autorDTO.setNome(postagem.getAutor().getNome());
        autorDTO.setNomeUsuario(postagem.getAutor().getNomeUsuario());

        // Mapeia os dados da postagem
        dto.setId(postagem.getId());
        dto.setConteudo(postagem.getConteudo());
        dto.setUrlMidia(postagem.getUrlMidia());
        dto.setDataCriacao(postagem.getDataCriacao());
        dto.setAutor(autorDTO); // Associa o DTO do autor

        return dto;
    }

    /**
     * Lista todas as postagens de um usuário específico, de forma paginada.
     *
     * @param username O nome de usuário do autor.
     * @param pageable Configurações de paginação.
     * @return Uma página de DTOs de postagem.
     */
    @Transactional(readOnly = true)
    public Page<PostagemResponseDTO> listarPostagensPorUsuario(String username, Pageable pageable) {
        // Encontra o usuário ou lança uma exceção
        Usuario autor = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + username));

        // Usa o método do repositório para buscar os posts pelo autor
        Page<Postagem> postagens = postagemRepository.findByAutor(autor, pageable);

        // Converte e retorna a página de DTOs
        return postagens.map(this::toResponseDTO);
    }
}