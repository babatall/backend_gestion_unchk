package com.tall.GestionUnchk.controller;

import com.tall.GestionUnchk.dto.LoginRequest;
import com.tall.GestionUnchk.dto.LoginResponse;
import com.tall.GestionUnchk.dto.SignupRequest;
import com.tall.GestionUnchk.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur d'authentification
 * Endpoints pour la connexion et l'inscription
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints d'authentification")
@SecurityRequirement(name = "")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Authentifie un utilisateur et retourne un token JWT
     */
    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne un token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Enregistre un nouvel utilisateur
     */
    @PostMapping("/signup")
    @Operation(summary = "Inscription utilisateur", description = "Crée un nouveau compte utilisateur")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Utilisateur enregistré avec succès");
    }
    
    /**
     * Endpoint de santé (pour vérifier que l'API fonctionne)
     */
    @GetMapping("/health")
    @Operation(summary = "Vérifier la santé de l'API", description = "Retourne le statut de l'API")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("L'API fonctionne correctement");
    }
}
