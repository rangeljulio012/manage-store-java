package com.manage_store_api.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // En producción, especifica dominios
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization") // Headers personalizados
                        .allowCredentials(false) // true si usas cookies/auth
                        .maxAge(3600); // Tiempo cache de opciones CORS
            }
        };
    }
}