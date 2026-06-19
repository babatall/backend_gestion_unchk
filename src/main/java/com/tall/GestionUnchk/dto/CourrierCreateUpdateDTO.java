package com.tall.GestionUnchk.dto;

import com.tall.GestionUnchk.enums.CourrierType;
import com.tall.GestionUnchk.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourrierCreateUpdateDTO {

    private String objet;

    private String description;

    private CourrierType type;

    private String expediteur;

    private Role destinataireRole;

    private LocalDate dateCourrier;

    private String fichier;
}
