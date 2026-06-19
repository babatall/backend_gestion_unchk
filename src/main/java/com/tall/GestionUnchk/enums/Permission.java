package com.tall.GestionUnchk.enums;

import lombok.Getter;

/**
 * Énumération des permissions disponibles.
 * Les permissions sont assignées aux rôles via la relation many-to-many.
 */
@Getter
public enum Permission {
    // Permissions User
    READ_USERS("Lire les utilisateurs"),
    CREATE_USER("Créer un utilisateur"),
    UPDATE_USER("Modifier un utilisateur"),
    DELETE_USER("Supprimer un utilisateur"),

    // Permissions Cours
    READ_COURSES("Lire les cours"),
    CREATE_COURSE("Créer un cours"),
    UPDATE_COURSE("Modifier un cours"),
    DELETE_COURSE("Supprimer un cours"),

    // Permissions Étudiants
    READ_STUDENTS("Lire les étudiants"),
    CREATE_STUDENT("Créer un étudiant"),
    UPDATE_STUDENT("Modifier un étudiant"),
    DELETE_STUDENT("Supprimer un étudiant"),

    // Permissions Enseignants
    READ_TEACHERS("Lire les enseignants"),
    CREATE_TEACHER("Créer un enseignant"),
    UPDATE_TEACHER("Modifier un enseignant"),
    DELETE_TEACHER("Supprimer un enseignant"),

    // Permissions Inscriptions
    READ_ENROLLMENTS("Lire les inscriptions"),
    CREATE_ENROLLMENT("Créer une inscription"),
    UPDATE_ENROLLMENT("Modifier une inscription"),
    DELETE_ENROLLMENT("Supprimer une inscription"),

    // Permissions Évaluations
    READ_GRADES("Lire les évaluations"),
    CREATE_GRADE("Créer une évaluation"),
    UPDATE_GRADE("Modifier une évaluation"),
    DELETE_GRADE("Supprimer une évaluation"),

    // Permissions Système
    MANAGE_ROLES("Gérer les rôles"),
    MANAGE_PERMISSIONS("Gérer les permissions"),
    GENERATE_REPORTS("Générer les rapports"),
    SYSTEM_ADMIN("Accès administrateur système");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

}
