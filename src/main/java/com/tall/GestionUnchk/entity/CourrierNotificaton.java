package com.tall.GestionUnchk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "courrier_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourrierNotificaton {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 2000)
    private String message;

    private Boolean lu;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "courrier_id")
    private Courrier courrier;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.lu = false;
    }
}
