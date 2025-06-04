package com.example.backend.config;

import com.example.backend.domain.Usuario; // <- Usar nossa entidade Usuario
import com.example.backend.repository.UsuarioRepository; // <- Usar nosso UsuarioRepository
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
@Profile("dev") // Executa apenas se o perfil "dev" estiver ativo
public class DbInitialization {

    // Removidas referências a ProdutoRepository e CategoriaRepository que não são usados aqui
    private final UsuarioRepository usuarioRepository; // <- Mudança aqui
    private final PasswordEncoder passwordEncoder;

    // Construtor usando os nomes corretos
    public DbInitialization(UsuarioRepository usuarioRepository, // <- Mudança aqui
                            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initDatabase() { // Removida a injeção duplicada, usará os campos da classe
        return args -> {
            // Verifica se já existem usuários para não duplicar
            if (this.usuarioRepository.count() == 0) { // <- Usa this.usuarioRepository
                System.out.println(">>>> Criando usuários de teste (admin e user)...");

                Usuario admin = new Usuario();
                admin.setNome("Administrador Principal");
                admin.setEmail("admin@admin.com");
                admin.setNomeUsuario("admin"); // Username para login
                admin.setSenha(passwordEncoder.encode("admin123")); // Senha a ser criptografada
                admin.setRoles(Set.of("ROLE_ADMIN", "ROLE_USER")); // Papéis
                admin.setEnabled(true);

                Usuario user = new Usuario();
                user.setNome("Usuário Comum");
                user.setEmail("user@user.com");
                user.setNomeUsuario("user"); // Username para login
                user.setSenha(passwordEncoder.encode("user123")); // Senha a ser criptografada
                user.setRoles(Set.of("ROLE_USER")); // Papel
                user.setEnabled(true);

                this.usuarioRepository.saveAll(List.of(admin, user)); // <- Usa this.usuarioRepository
                System.out.println(">>>> Usuários de teste criados com sucesso!");
            } else {
                System.out.println(">>>> Usuários de teste já existem ou a tabela não está vazia.");
            }
        };
    }
}