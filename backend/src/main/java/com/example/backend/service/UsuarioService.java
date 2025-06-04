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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Transactional
    public Usuario registrarNovoUsuario(UsuarioRegistroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Erro: Email já está em uso!");
        }
        if (usuarioRepository.existsByNomeUsuario(dto.getNomeUsuario())) {
            throw new IllegalArgumentException("Erro: Nome de usuário já está em uso!");
        }

        log.info("Tentando registrar novo usuário: {}", dto.getNomeUsuario());

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setNomeUsuario(dto.getNomeUsuario());
        novoUsuario.setTelefone(dto.getTelefone());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setEnabled(true); // Habilitado por padrão

        // Atribuição de papéis
        Set<String> rolesParaSalvar = new HashSet<>();
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            // Adiciona os papéis fornecidos, garantindo o prefixo ROLE_ se necessário
            // (A lógica do prefixo pode ser mais elaborada se os papéis vêm de um enum, etc.)
            for (String role : dto.getRoles()) {
                rolesParaSalvar.add(role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase());
            }
        } else {
            rolesParaSalvar.add("ROLE_USER"); // Papel padrão se nenhum for fornecido
        }
        novoUsuario.setRoles(rolesParaSalvar);

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        log.info("Usuário {} registrado com papéis {} e ID {}",
                usuarioSalvo.getNomeUsuario(), usuarioSalvo.getRoles(), usuarioSalvo.getId());
        return usuarioSalvo;
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

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        log.info("Listando todos os usuários do banco de dados");
        List<Usuario> usuarios = usuarioRepository.findAll(); // Usa o método REAL do JpaRepository
        log.info("Total de usuários encontrados no banco: {}", usuarios.size());
        return usuarios;
    }


    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByNomeUsuario(String nomeUsuario) {
        return usuarioRepository.existsByNomeUsuario(nomeUsuario);
    }

}