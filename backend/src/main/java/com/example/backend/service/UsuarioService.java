package com.example.backend.service;

import com.example.backend.domain.AreaDeAtuacao;
import com.example.backend.domain.Perfil;
import com.example.backend.domain.Usuario;
import com.example.backend.dto.*;
import com.example.backend.repository.AreaDeAtuacaoRepository;
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
    private final AreaDeAtuacaoRepository areaDeAtuacaoRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                          AvaliacaoService avaliacaoService, AreaDeAtuacaoRepository areaDeAtuacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.avaliacaoService = avaliacaoService;
        this.areaDeAtuacaoRepository = areaDeAtuacaoRepository;
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
        rolesParaSalvar.add("ROLE_USER");
        novoUsuario.setRoles(rolesParaSalvar);

        return usuarioRepository.save(novoUsuario);
    }

    @Transactional(readOnly = true)
    public PerfilResponseDTO buscarPerfilPorUsername(String nomeUsuario, String usuarioLogadoUsername) {
        Usuario usuarioDoPerfil = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o nome: " + nomeUsuario));
        return buildPerfilResponseDTO(usuarioDoPerfil, usuarioLogadoUsername);
    }

    @Transactional
    public PerfilResponseDTO atualizarPerfilCompleto(String usernameAtual, PerfilUpdateRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(usernameAtual)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usernameAtual));

        if (dto.getNome() != null && !dto.getNome().isBlank()) usuario.setNome(dto.getNome());
        if (dto.getNomeUsuario() != null && !dto.getNomeUsuario().isBlank() && !dto.getNomeUsuario().equals(usernameAtual)) {
            if (usuarioRepository.existsByNomeUsuario(dto.getNomeUsuario())) {
                throw new IllegalArgumentException("Erro: Nome de usuário já está em uso!");
            }
            usuario.setNomeUsuario(dto.getNomeUsuario());
        }

        Perfil perfil = usuario.getPerfil();
        if (perfil == null) {
            perfil = new Perfil();
            perfil.setUsuario(usuario);
            usuario.setPerfil(perfil);
        }
        if (dto.getBio() != null) perfil.setBio(dto.getBio());
        if (dto.getFormacao() != null) perfil.setFormacao(dto.getFormacao());
        if (dto.getFotoPerfilUrl() != null) perfil.setFotoPerfilUrl(dto.getFotoPerfilUrl());
        if (dto.getFotoCapaUrl() != null) perfil.setFotoCapaUrl(dto.getFotoCapaUrl());
        if (dto.getIsProfissional() != null) perfil.setProfissional(dto.getIsProfissional());

        if (dto.getAreasDeAtuacaoIds() != null) {
            Set<AreaDeAtuacao> novasAreas = new HashSet<>(areaDeAtuacaoRepository.findAllById(dto.getAreasDeAtuacaoIds()));
            usuario.setAreasDeAtuacao(novasAreas);
        }

        usuarioRepository.save(usuario);
        return buildPerfilResponseDTO(usuario, usernameAtual);
    }

    @Transactional
    public void seguirUsuario(String usernameSeguidor, String usernameParaSeguir) {
        if (usernameSeguidor.equalsIgnoreCase(usernameParaSeguir)) {
            throw new IllegalArgumentException("Você não pode seguir a si mesmo.");
        }
        Usuario seguidor = usuarioRepository.findByNomeUsuarioWithSeguindo(usernameSeguidor)
                .orElseThrow(() -> new EntityNotFoundException("Usuário seguidor não encontrado: " + usernameSeguidor));
        Usuario paraSeguir = usuarioRepository.findByNomeUsuario(usernameParaSeguir)
                .orElseThrow(() -> new EntityNotFoundException("Usuário a ser seguido não encontrado."));

        if (!seguidor.getSeguindo().contains(paraSeguir)) {
            seguidor.getSeguindo().add(paraSeguir);
        }
    }

    @Transactional
    public void deixarDeSeguirUsuario(String usernameSeguidor, String usernameParaDeixarDeSeguir) {
        Usuario seguidor = usuarioRepository.findByNomeUsuarioWithSeguindo(usernameSeguidor)
                .orElseThrow(() -> new EntityNotFoundException("Usuário seguidor não encontrado: " + usernameSeguidor));
        Usuario paraDeixarDeSeguir = usuarioRepository.findByNomeUsuario(usernameParaDeixarDeSeguir)
                .orElseThrow(() -> new EntityNotFoundException("Usuário a ser deixado de seguir não encontrado."));

        seguidor.getSeguindo().remove(paraDeixarDeSeguir);
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

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> buscarUsuarios(String query, Long areaId, Pageable pageable) {
        Specification<Usuario> spec = UsuarioSpecification.comFiltros(query, areaId);
        Page<Usuario> usuarios = usuarioRepository.findAll(spec, pageable);
        return usuarios.map(this::converterUsuarioParaResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> buscarProfissionais(Pageable pageable) {
        return usuarioRepository.findByPerfilIsProfissional(true, pageable).map(this::converterUsuarioParaResponseDTO);
    }

    private PerfilResponseDTO buildPerfilResponseDTO(Usuario usuarioDoPerfil, String usuarioLogadoUsername) {
        Double notaMedia = avaliacaoService.getNotaMediaPorUsuario(usuarioDoPerfil);
        long totalAvaliacoes = avaliacaoService.getTotalAvaliacoesPorUsuario(usuarioDoPerfil);
        int totalSeguidores = usuarioRepository.countSeguidoresById(usuarioDoPerfil.getId());
        int totalSeguindo = usuarioRepository.countSeguindoById(usuarioDoPerfil.getId());

        PerfilResponseDTO dto = new PerfilResponseDTO();
        dto.setId(usuarioDoPerfil.getId());
        dto.setNome(usuarioDoPerfil.getNome());
        dto.setNomeUsuario(usuarioDoPerfil.getNomeUsuario());
        dto.setEmail(usuarioDoPerfil.getEmail());
        dto.setRoles(usuarioDoPerfil.getRoles());
        dto.setNotaMedia(notaMedia);
        dto.setTotalAvaliacoes(totalAvaliacoes);
        dto.setTotalSeguidores(totalSeguidores);
        dto.setTotalSeguindo(totalSeguindo);

        boolean estaSeguindo = false;
        if (usuarioLogadoUsername != null && !usuarioLogadoUsername.equals(usuarioDoPerfil.getNomeUsuario())) {
            estaSeguindo = usuarioRepository.findByNomeUsuarioWithSeguindo(usuarioLogadoUsername)
                    .map(u -> u.getSeguindo().contains(usuarioDoPerfil))
                    .orElse(false);
        }
        dto.setSeguindoPeloUsuarioLogado(estaSeguindo);

        if (usuarioDoPerfil.getPerfil() != null) {
            dto.setBio(usuarioDoPerfil.getPerfil().getBio());
            dto.setFormacao(usuarioDoPerfil.getPerfil().getFormacao());
            dto.setFotoPerfilUrl(usuarioDoPerfil.getPerfil().getFotoPerfilUrl());
            dto.setFotoCapaUrl(usuarioDoPerfil.getPerfil().getFotoCapaUrl());
            dto.setProfissional(usuarioDoPerfil.getPerfil().isProfissional());
        }

        if (usuarioDoPerfil.getAreasDeAtuacao() != null && !usuarioDoPerfil.getAreasDeAtuacao().isEmpty()) {
            List<AreaDeAtuacaoResponseDTO> areasDTO = usuarioDoPerfil.getAreasDeAtuacao().stream()
                    .map(area -> new AreaDeAtuacaoResponseDTO(area.getId(), area.getNome()))
                    .collect(Collectors.toList());
            dto.setAreasDeAtuacao(areasDTO);
        }
        return dto;
    }

    private UsuarioResponseDTO converterUsuarioParaResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setNomeUsuario(usuario.getNomeUsuario());

        if (usuario.getPerfil() != null) {
            dto.setFotoPerfilUrl(usuario.getPerfil().getFotoPerfilUrl());
            dto.setProfissional(usuario.getPerfil().isProfissional());
        }

        if (usuario.getAreasDeAtuacao() != null && !usuario.getAreasDeAtuacao().isEmpty()) {
            List<AreaDeAtuacaoResponseDTO> areasDTO = usuario.getAreasDeAtuacao().stream()
                    .map(area -> new AreaDeAtuacaoResponseDTO(area.getId(), area.getNome()))
                    .collect(Collectors.toList());
            dto.setAreasDeAtuacao(areasDTO);
        }

        Double notaMedia = avaliacaoService.getNotaMediaPorUsuario(usuario);
        dto.setNotaMedia(notaMedia);

        return dto;
    }
}