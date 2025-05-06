package com.example.backend.config;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyLogger {

    private static final Logger log = LoggerFactory.getLogger(PropertyLogger.class);

    // Injeta o valor da propriedade ddl-auto
    @Value("${spring.jpa.hibernate.ddl-auto:not-set}")
    private String ddlAutoValue;

    // Injeta os perfis ativos (ou 'none' se nenhum)
    @Value("${spring.profiles.active:none}")
    private String activeProfiles;

    @PostConstruct
    public void logProperties() {
        log.warn("----------------------------------------------------------");
        log.warn("VERIFICANDO PROPRIEDADES EFETIVAS NA INICIALIZAÇÃO:");
        log.warn("O valor REALMENTE USADO para 'spring.jpa.hibernate.ddl-auto' é: '{}'", ddlAutoValue);
        log.warn("Perfis ativos ('spring.profiles.active'): '{}'", activeProfiles);
        log.warn("----------------------------------------------------------");
    }
}