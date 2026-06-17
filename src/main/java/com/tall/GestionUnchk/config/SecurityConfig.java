package com.tall.GestionUnchk.config;

import com.tall.GestionUnchk.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration Spring Security
 * Responsabilités:
 * - Configuration du chiffrement des mots de passe
 * - Configuration des règles d'autorisation
 * - Intégration du filtre JWT
 * - Gestion des sessions (stateless)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    /**
     * Encodeur de mots de passe BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * AuthenticationManager pour gérer l'authentification
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    /**
     * Filtre JWT personnalisé
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    
    /**
     * Configuration de la chaîne de filtres de sécurité
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactiver CSRF (pas nécessaire pour une API stateless)
                .csrf(csrf -> csrf.disable())
                
                // Désactiver CORS (à configurer séparément si nécessaire)
               .cors(cors -> {})
                
                // Politique de session stateless (JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Configuration des autorisations
                .authorizeHttpRequests(authz -> authz
                        // Endpoints publics - Authentification
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/api/v1/auth/**").permitAll()
                     
                        
                        // Swagger/OpenAPI
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        
                        // Health check
                        .requestMatchers("/api/v1/health").permitAll()
                        
                        // Endpoints User - Requis authentification
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole("ADMIN", "PERSONNEL_ADMINISTRATIF")
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                        
                        // Endpoints Student
                        .requestMatchers(HttpMethod.GET, "/api/v1/students/**").hasAnyRole("ADMIN", "ENSEIGNANT", "ETUDIANT")
                        .requestMatchers(HttpMethod.POST, "/api/v1/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/students/**").hasRole("ADMIN")
                        
                        // Endpoints Teacher
                        .requestMatchers(HttpMethod.GET, "/api/v1/teachers/**").hasAnyRole("ADMIN", "ENSEIGNANT", "ETUDIANT")
                        .requestMatchers(HttpMethod.POST, "/api/v1/teachers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/teachers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/teachers/**").hasRole("ADMIN")
                        
                        // Endpoints Course
                        .requestMatchers(HttpMethod.GET, "/api/v1/courses/**").hasAnyRole("ADMIN", "ENSEIGNANT", "ETUDIANT")
                        .requestMatchers(HttpMethod.POST, "/api/v1/courses").hasAnyRole("ADMIN", "ENSEIGNANT")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/courses/**").hasAnyRole("ADMIN", "ENSEIGNANT")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/courses/**").hasRole("ADMIN")
                        
                        // Endpoints Enrollment
                        .requestMatchers(HttpMethod.GET, "/api/v1/enrollments/**").hasAnyRole("ADMIN", "ENSEIGNANT", "ETUDIANT")
                        .requestMatchers(HttpMethod.POST, "/api/v1/enrollments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/enrollments/**").hasRole("ADMIN")
                        
                        // Endpoints Grade
                        .requestMatchers(HttpMethod.GET, "/api/v1/grades/**").hasAnyRole("ADMIN", "ENSEIGNANT", "ETUDIANT")
                        .requestMatchers(HttpMethod.POST, "/api/v1/grades").hasAnyRole("ADMIN", "ENSEIGNANT")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/grades/**").hasAnyRole("ADMIN", "ENSEIGNANT")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/grades/**").hasRole("ADMIN")
                        
                        // Tout le reste requis l'authentification
                        .anyRequest().authenticated()
                )
                
                // Exceptions d'authentification
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.getWriter().write("{\"error\": \"Access Denied\"}");
                        })
                );
        
        // Ajouter le filtre JWT avant le filtre d'authentification
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(List.of("*"));

    configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
    );

    configuration.setAllowedHeaders(List.of("*"));

    configuration.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return source;
}
}
