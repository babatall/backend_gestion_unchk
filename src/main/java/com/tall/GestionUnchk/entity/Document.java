package com.tall.GestionUnchk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;

    private String objet;

    @Column(length = 2000)
    private String contenu;

    private String expediteur;

    private String destinataire;

    private LocalDate dateDocument;

    @Enumerated(EnumType.STRING)
    private DocumentType type;
}