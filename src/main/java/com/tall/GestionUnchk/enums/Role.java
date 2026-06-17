package com.tall.GestionUnchk.enums;

/**
 * Énumération des rôles utilisateurs du système.
 * Chaque rôle a des permissions spécifiques définis dans la base de données.
 */
public enum Role {
    ADMIN("Administrateur du système"),
    RECTEUR("Recteur de l'université"),
    DOYEN("Doyen de faculté"),
    CHEF_DEPARTMENT("Chef de département"),
    ENSEIGNANT("Enseignant"),
    ETUDIANT("Étudiant"),
    PERSONNEL_ADMINISTRATIF("Personnel administratif");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
