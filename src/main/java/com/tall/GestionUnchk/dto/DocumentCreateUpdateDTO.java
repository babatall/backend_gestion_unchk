package com.tall.GestionUnchk.dto;

import com.tall.GestionUnchk.entity.DocumentType;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentCreateUpdateDTO {

    private String reference;

    private String objet;

    private String contenu;

    private String expediteur;

    private String destinataire;

    private LocalDate dateDocument;

    private DocumentType type;
}