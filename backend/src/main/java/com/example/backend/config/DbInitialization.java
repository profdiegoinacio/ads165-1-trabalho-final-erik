package com.example.backend.config;

import com.example.backend.domain.AreaDeAtuacao;
import com.example.backend.domain.Usuario;
import com.example.backend.repository.AreaDeAtuacaoRepository;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@Profile("dev")
public class DbInitialization {

    @Bean
    public CommandLineRunner inicializarDados(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AreaDeAtuacaoRepository areaDeAtuacaoRepository) {

        return args -> {
            if (areaDeAtuacaoRepository.count() == 0) {
                System.out.println(">>>> Criando um conjunto inicial de Áreas de Atuação...");

                List<String> nomesDasAreas = List.of(
                        "Design Gráfico", "Desenvolvimento Web", "Marketing Digital", "Elétrica Predial",
                        "Fotografia", "Edição de Vídeo", "Redação e Copywriting", "Consultoria de Negócios",
                        "Contabilidade", "Marcenaria", "Jardinagem", "Aulas Particulares", "Encanamento",
                        "Pintura Residencial", "Desenvolvimento Mobile"
                );

                List<AreaDeAtuacao> areas = nomesDasAreas.stream().map(nome -> {
                    AreaDeAtuacao area = new AreaDeAtuacao();
                    area.setNome(nome);
                    return area;
                }).collect(Collectors.toList());

                areaDeAtuacaoRepository.saveAll(areas);
                System.out.println(">>>> " + areas.size() + " áreas de atuação criadas com sucesso!");
            }
            if (usuarioRepository.count() == 0) {
                System.out.println(">>>> Criando usuários de teste (admin e user)...");

                AreaDeAtuacao design = areaDeAtuacaoRepository.findById(1L).orElse(null);
                AreaDeAtuacao webdev = areaDeAtuacaoRepository.findById(2L).orElse(null);
                AreaDeAtuacao marketing = areaDeAtuacaoRepository.findById(3L).orElse(null);

                Usuario admin = new Usuario();
                admin.setNome("Administrador Principal");
                admin.setEmail("admin@conectapro.com");
                admin.setNomeUsuario("admin");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of("ROLE_ADMIN", "ROLE_USER"));
                if (webdev != null && marketing != null) {
                    admin.setAreasDeAtuacao(Set.of(webdev, marketing));
                }
                admin.setEnabled(true);

                Usuario user = new Usuario();
                user.setNome("Usuário Profissional");
                user.setEmail("user@conectapro.com");
                user.setNomeUsuario("user");
                user.setSenha(passwordEncoder.encode("user123"));
                user.setRoles(Set.of("ROLE_USER"));
                if (design != null) {
                    user.setAreasDeAtuacao(Set.of(design));
                }
                user.setEnabled(true);

                usuarioRepository.saveAll(List.of(admin, user));
                System.out.println(">>>> Usuários de teste criados com sucesso!");
            }
        };
    }
}