package com.example.backend.config; // Certifique-se que o pacote está correto

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Marca esta classe como uma fonte de configurações de Beans Spring
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**") // Define o padrão de URL da API que terá CORS aplicado
                // Ajuste o padrão se sua API não começar com /api/v1/**

                .allowedOrigins("http://localhost:3000") // Permite requisições SOMENTE desta origem (seu frontend Next.js em desenvolvimento)
                // IMPORTANTE: Para produção, troque para a URL do seu frontend real!

                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH") // Métodos HTTP permitidos
                // OPTIONS é importante para requisições "preflight" de CORS

                .allowedHeaders("*") // Permite todos os cabeçalhos na requisição
                // Para mais segurança em produção, pode listar cabeçalhos específicos

                // .allowCredentials(true) // Descomente se precisar enviar cookies ou usar autenticação HTTP básica
                // Geralmente não necessário para autenticação baseada em Token (Bearer)

                .maxAge(3600); // Tempo (em segundos) que o navegador pode cachear a resposta preflight (OPTIONS)
    }
}