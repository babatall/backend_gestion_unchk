package com.tall.GestionUnchk.entity;

import com.tall.GestionUnchk.enums.CourrierType;
import com.tall.GestionUnchk.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "courriers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Courrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;

    private String objet;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private CourrierType type;

    private String expediteur;

    @Enumerated(EnumType.STRING)
    private Role destinataireRole;

    private LocalDate dateCourrier;

    private String fichier;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}