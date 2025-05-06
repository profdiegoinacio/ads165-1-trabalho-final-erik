package com.example.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails { // Implementa UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nomeUsuario;
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Define os papéis/permissões do usuário. Pecisa ser tratado melhor
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha criptografada
    }

    @Override
    public String getUsername() {
        return this.nomeUsuario; // Retorna o campo que identifica o usuário
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Por padrão, a conta não expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Por padrão, a conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Por padrão, as credenciais não expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // Por padrão, o usuário está habilitado
    }
}