package com.tall.GestionUnchk.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger/OpenAPI 3.0
 * Fournit la documentation interactive de l'API
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // Information générale
                .info(new Info()
                        .title("API Gestion Universitaire UNCHK")
                        .description("Platform de gestion universitaire avec authentification JWT")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Support")
                                .email("support@unchk.edu.sn")
                                .url("https://unchk.edu.sn")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                )
                
                // Configuration du schéma de sécurité JWT
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Token - Obtenu via /api/v1/auth/login")
                        )
                )
                
                // Appliquer le schéma JWT globalement
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-jwt")
                );
    }
}
