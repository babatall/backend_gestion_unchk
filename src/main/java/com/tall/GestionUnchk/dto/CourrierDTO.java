package com.tall.GestionUnchk.dto;

import com.tall.GestionUnchk.enums.CourrierType;
import com.tall.GestionUnchk.enums.Role;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class CourrierDTO {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String reference;

        private String objet;

        private String description;

        private CourrierType type;

        private String expediteur;

        private Role destinataireRole;

        private LocalDate dateCourrier;

        private String fichier;
}
