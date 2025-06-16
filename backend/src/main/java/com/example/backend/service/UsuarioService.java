package com.example.backend.service;

import com.example.backend.domain.Perfil;
import com.example.backend.domain.Usuario;
import com.example.backend.dto.PerfilResponseDTO;
import com.example.backend.dto.PerfilUpdateRequestDTO;
import com.example.backend.dto.UsuarioRegistroDTO;
import com.example.backend.dto.UsuarioResponseDTO;
import com.example.backend.dto.UsuarioUpdateRequestDTO;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.repository.specification.UsuarioSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AvaliacaoService avaliacaoService;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AvaliacaoService avaliacaoService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.avaliacaoService = avaliacaoService;
    }

    @Transactional
    public Usuario registrarNovoUsuario(UsuarioRegistroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Erro: Email já está em uso!");
        }
        if (usuarioRepository.existsByNomeUsuario(dto.getNomeUsuario())) {
            throw new IllegalArgumentException("Erro: Nome de usuário já está em uso!");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setNomeUsuario(dto.getNomeUsuario());
        novoUsuario.setTelefone(dto.getTelefone());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setEnabled(true);

        Perfil novoPerfil = new Perfil();
        novoPerfil.setUsuario(novoUsuario);
        novoUsuario.setPerfil(novoPerfil);

        Set<String> rolesParaSalvar = new HashSet<>();
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            dto.getRoles().forEach(role -> rolesParaSalvar.add(role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase()));
        } else {
            rolesParaSalvar.add("ROLE_USER");
        }
        novoUsuario.setRoles(rolesParaSalvar);

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        log.info("Usuário {} registrado com papéis {} e ID {}", usuarioSalvo.getNomeUsuario(), usuarioSalvo.getRoles(), usuarioSalvo.getId());
        return usuarioSalvo;
    }

    @Transactional(readOnly = true)
    public PerfilResponseDTO buscarPerfilPorUsername(String nomeUsuario, String usuarioLogadoUsername) {
        Usuario usuarioDoPerfil = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o nome: " + nomeUsuario));
        return buildPerfilResponseDTO(usuarioDoPerfil, usuarioLogadoUsername);
    }

    @Transactional
    public PerfilResponseDTO atualizarDadosPrincipais(String usernameAtual, UsuarioUpdateRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(usernameAtual)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usernameAtual));

        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getNomeUsuario() != null && !dto.getNomeUsuario().isEmpty() && !dto.getNomeUsuario().equals(usernameAtual)) {
            if (usuarioRepository.existsByNomeUsuario(dto.getNomeUsuario())) {
                throw new IllegalArgumentException("Erro: Nome de usuário já está em uso!");
            }
            usuario.setNomeUsuario(dto.getNomeUsuario());
        }
        usuarioRepository.save(usuario);
        return buildPerfilResponseDTO(usuario, usernameAtual);
    }

    @Transactional
    public PerfilResponseDTO atualizarPerfil(String username, PerfilUpdateRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + username));

        Perfil perfilParaAtualizar = usuario.getPerfil();
        if (perfilParaAtualizar == null) {
            perfilParaAtualizar = new Perfil();
            perfilParaAtualizar.setUsuario(usuario);
            usuario.setPerfil(perfilParaAtualizar);
        }

        if (dto.getBio() != null) perfilParaAtualizar.setBio(dto.getBio());
        if (dto.getFormacao() != null) perfilParaAtualizar.setFormacao(dto.getFormacao());
        if (dto.getFotoPerfilUrl() != null) perfilParaAtualizar.setFotoPerfilUrl(dto.getFotoPerfilUrl());
        if (dto.getFotoCapaUrl() != null) perfilParaAtualizar.setFotoCapaUrl(dto.getFotoCapaUrl());

        usuarioRepository.save(usuario);
        return buildPerfilResponseDTO(usuario, username);
    }

    @Transactional
    public void seguirUsuario(String usernameSeguidor, String usernameParaSeguir) {
        if (usernameSeguidor.equalsIgnoreCase(usernameParaSeguir)) {
            throw new IllegalArgumentException("Você não pode seguir a si mesmo.");
        }
        Usuario seguidor = usuarioRepository.findByNomeUsuario(usernameSeguidor)
                .orElseThrow(() -> new EntityNotFoundException("Usuário seguidor não encontrado."));
        Usuario paraSeguir = usuarioRepository.findByNomeUsuario(usernameParaSeguir)
                .orElseThrow(() -> new EntityNotFoundException("Usuário a ser seguido não encontrado."));
        seguidor.getSeguindo().add(paraSeguir);
    }

    @Transactional
    public void deixarDeSeguirUsuario(String usernameSeguidor, String usernameParaDeixarDeSeguir) {
        Usuario seguidor = usuarioRepository.findByNomeUsuario(usernameSeguidor)
                .orElseThrow(() -> new EntityNotFoundException("Usuário seguidor não encontrado."));
        Usuario paraDeixarDeSeguir = usuarioRepository.findByNomeUsuario(usernameParaDeixarDeSeguir)
                .orElseThrow(() -> new EntityNotFoundException("Usuário a ser deixado de seguir não encontrado."));
        seguidor.getSeguindo().remove(paraDeixarDeSeguir);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> buscarUsuarios(String query, Pageable pageable) {
        Specification<Usuario> spec = UsuarioSpecification.comFiltroDeNome(query);
        Page<Usuario> usuarios = usuarioRepository.findAll(spec, pageable);
        return usuarios.map(UsuarioResponseDTO::new);
    }

    // Método helper privado para construir o PerfilResponseDTO, agora com ambos os parâmetros
    private PerfilResponseDTO buildPerfilResponseDTO(Usuario usuarioDoPerfil, String usuarioLogadoUsername) {
        Double notaMedia = avaliacaoService.getNotaMediaPorUsuario(usuarioDoPerfil);
        long totalAvaliacoes = avaliacaoService.getTotalAvaliacoesPorUsuario(usuarioDoPerfil);

        PerfilResponseDTO dto = new PerfilResponseDTO();
        dto.setId(usuarioDoPerfil.getId());
        dto.setNome(usuarioDoPerfil.getNome());
        dto.setNomeUsuario(usuarioDoPerfil.getNomeUsuario());
        dto.setEmail(usuarioDoPerfil.getEmail());
        dto.setRoles(usuarioDoPerfil.getRoles());
        dto.setNotaMedia(notaMedia);
        dto.setTotalAvaliacoes(totalAvaliacoes);

        boolean estaSeguindo = false;
        if (usuarioLogadoUsername != null && !usuarioLogadoUsername.equals(usuarioDoPerfil.getNomeUsuario())) {
            estaSeguindo = usuarioRepository.findByNomeUsuario(usuarioLogadoUsername)
                    .map(u -> u.getSeguindo().contains(usuarioDoPerfil))
                    .orElse(false);
        }
        dto.setSeguindoPeloUsuarioLogado(estaSeguindo);

        if (usuarioDoPerfil.getPerfil() != null) {
            dto.setBio(usuarioDoPerfil.getPerfil().getBio());
            dto.setFormacao(usuarioDoPerfil.getPerfil().getFormacao());
            dto.setFotoPerfilUrl(usuarioDoPerfil.getPerfil().getFotoPerfilUrl());
            dto.setFotoCapaUrl(usuarioDoPerfil.getPerfil().getFotoCapaUrl());
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        log.info("Buscando usuário por ID: {}", id);
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        log.info("Listando todos os usuários do banco de dados");
        return usuarioRepository.findAll();
    }
}