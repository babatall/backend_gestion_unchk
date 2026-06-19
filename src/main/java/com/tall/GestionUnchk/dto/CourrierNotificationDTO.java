package com.tall.GestionUnchk.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourrierNotificationDTO {

    private Long id;
    private String titre;
    private String message;
    private Boolean lu;
    private LocalDateTime createdAt;

    private Long courrierId;
    private String courrierReference;
    private String courrierObjet;
}