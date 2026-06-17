package com.tall.GestionUnchk.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtre JWT qui intercepte les requêtes HTTP
 * Responsabilités:
 * - Extraction du token du header Authorization
 * - Validation du token
 * - Chargement de l'utilisateur
 * - Configuration de l'authentification dans le contexte de sécurité
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extraction du JWT du header Authorization
            String jwt = extractJwtFromRequest(request);
            
            // Validation du token
            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
                
                // Extraction du username du token
                String username = jwtService.getUsernameFromToken(jwt);
                
                // Chargement des détails de l'utilisateur
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Création de l'objet d'authentification
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                
                // Configuration des détails de la requête
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                // Configuration du contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Utilisateur authentifié: {}", username);
            }
        } catch (Exception ex) {
            log.error("Erreur lors de l'authentification JWT: {}", ex.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Extrait le JWT du header Authorization
     * Format: Authorization: Bearer <token>
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
}
