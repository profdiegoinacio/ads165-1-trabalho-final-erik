package com.example.backend.service;

import com.example.backend.domain.Usuario;
import com.example.backend.dto.UsuarioRegistroDTO;
import com.example.backend.repository.UsuarioRepository; // <-- DESCOMENTE/USE o repositório
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // Pode usar Autowired ou construtor
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- DESCOMENTE/USE o encoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- USE O IMPORT DO SPRING

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService{ // REMOVIDO UserDetailsService daqui, ficará no AutenticacaoService

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    // --- Remova a simulação em memória ---
    // private final Map<Long, Usuario> usuariosEmMemoria = new ConcurrentHashMap<>();
    // private final AtomicLong idGenerator = new AtomicLong(0);
    // -----------------------------------

    // --- Injeção de Dependências via Construtor (Recomendado) ---
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Construtor para injeção
    @Autowired // Ou remova @Autowired se preferir apenas injeção via construtor padrão do Spring
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // ---------------------------------------------------------

    @Transactional // Import de org.springframework.transaction.annotation.Transactional
    public Usuario registrarNovoUsuario(UsuarioRegistroDTO dto) {
        // 1. Validar se email ou nomeUsuario já existem
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Erro: Email já está em uso!");
        }
        if (usuarioRepository.existsByNomeUsuario(dto.getNomeUsuario())) {
            throw new IllegalArgumentException("Erro: Nome de usuário já está em uso!");
        }

        log.info("Tentando registrar novo usuário: {}", dto.getNomeUsuario());

        Usuario novoUsuario = new Usuario();
        // ID será gerado pelo banco
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setNomeUsuario(dto.getNomeUsuario());
        novoUsuario.setTelefone(dto.getTelefone());

        // 2. Criptografar a senha ANTES de salvar
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        // 3. Salvar no banco de dados usando o repositório REAL
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        log.info("Usuário {} registrado com ID {}", usuarioSalvo.getNomeUsuario(), usuarioSalvo.getId());
        return usuarioSalvo; // Retorna o usuário salvo pelo repositório
    }

    @Transactional(readOnly = true) // Opcional: Marcar leituras como readOnly
    public Optional<Usuario> buscarPorId(Long id) {
        log.info("Buscando usuário por ID no banco: {}", id);
        Optional<Usuario> usuario = usuarioRepository.findById(id); // Usa o método REAL do JpaRepository
        if(usuario.isPresent()) {
            log.info("Usuário encontrado no banco: {}", usuario.get().getNomeUsuario());
        } else {
            log.warn("Usuário com ID {} não encontrado no banco.", id);
        }
        return usuario;
    }

    @Transactional(readOnly = true) // Opcional: Marcar leituras como readOnly
    public List<Usuario> listarTodos() {
        log.info("Listando todos os usuários do banco de dados");
        List<Usuario> usuarios = usuarioRepository.findAll(); // Usa o método REAL do JpaRepository
        log.info("Total de usuários encontrados no banco: {}", usuarios.size());
        return usuarios;
    }

    // Métodos auxiliares que usam o repositório real
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByNomeUsuario(String nomeUsuario) {
        return usuarioRepository.existsByNomeUsuario(nomeUsuario);
    }

    // Removi a implementação de UserDetailsService daqui para ficar no AutenticacaoService
}