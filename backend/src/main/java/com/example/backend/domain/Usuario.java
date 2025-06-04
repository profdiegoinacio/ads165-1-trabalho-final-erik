package com.example.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet; // Import HashSet
import java.util.Set;     // Import Set
import java.util.stream.Collectors; // Import Collectors

@Entity
@Table(name = "usuarios") // Mantido como "usuarios"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // Mantido

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nomeUsuario; // Usaremos como username para UserDetails

    private String telefone;

    @Column(nullable = false)
    private String senha; // Senha já criptografada

    private boolean enabled = true; // Campo do exemplo do texto, útil

    // Novo campo para papéis
    @ElementCollection(fetch = FetchType.EAGER) // Carrega os papéis junto com o usuário
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>(); // Ex: "ROLE_USER", "ROLE_ADMIN"

    // Construtor customizado para registro (exemplo)
    public Usuario(String nome, String email, String nomeUsuario, String telefone, String senha, Set<String> roles) {
        this.nome = nome;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.telefone = telefone;
        this.senha = senha; // Senha deve ser passada já criptografada para este construtor
        this.roles = roles;
        this.enabled = true;
    }


    // --- Implementação dos métodos UserDetails ATUALIZADA ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapeia os papéis armazenados para GrantedAuthority
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        // Continuaremos usando nomeUsuario como o "username" para Spring Security
        return this.nomeUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}