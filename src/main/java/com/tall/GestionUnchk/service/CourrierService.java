package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.CourrierCreateUpdateDTO;
import com.tall.GestionUnchk.dto.CourrierDTO;
import com.tall.GestionUnchk.entity.Courrier;
import com.tall.GestionUnchk.entity.CourrierNotificaton;
import com.tall.GestionUnchk.entity.User;
import com.tall.GestionUnchk.enums.CourrierType;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.CourrierRepository;
import com.tall.GestionUnchk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourrierService {

    private final CourrierRepository courrierRepository;
    private  final UserRepository userRepository;
    private  final CourrierNotificationService courrierNotificationService;

    public List<CourrierDTO> getAll() {

        return courrierRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public CourrierDTO getById(Long id) {

        Courrier courrier = courrierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Courrier introuvable"
                        ));

        return convertToDTO(courrier);
    }

    public CourrierDTO create(
            CourrierCreateUpdateDTO dto
    ) {

        Courrier courrier = Courrier.builder()
                .reference(generateReference(dto.getType()))
                .objet(dto.getObjet())
                .description(dto.getDescription())
                .type(dto.getType())
                .expediteur(dto.getExpediteur())
                .destinataireRole(dto.getDestinataireRole())
                .dateCourrier(dto.getDateCourrier())
                .fichier(dto.getFichier())
                .build();

        courrier = courrierRepository.save(courrier);

        /*
         * Distribution automatique
         */
        List<User> destinataires =
                userRepository.findByRoles_Name(
                        dto.getDestinataireRole()
                );

        for (User user : destinataires) {

            courrierNotificationService
                    .envoyerNotification(
                            user,
                            courrier
                    );
        }

        log.info(
                "Courrier créé {} envoyé à {} utilisateurs",
                courrier.getReference(),
                destinataires.size()
        );

        return convertToDTO(courrier);
    }

    private @NonNull Courrier getCourrier(Courrier courrier) {
        courrier = courrierRepository.save(courrier);
        return courrier;
    }

    public CourrierDTO update(
            Long id,
            CourrierCreateUpdateDTO dto
    ) {

        Courrier courrier = courrierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Courrier introuvable"
                        ));

        courrier.setObjet(dto.getObjet());
        courrier.setDescription(dto.getDescription());
        courrier.setType(dto.getType());
        courrier.setExpediteur(dto.getExpediteur());
        courrier.setDestinataireRole(dto.getDestinataireRole());
        courrier.setDateCourrier(dto.getDateCourrier());
        courrier.setFichier(dto.getFichier());

        courrier = courrierRepository.save(courrier);

        return convertToDTO(courrier);
    }

    public void delete(Long id) {

        Courrier courrier = courrierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Courrier introuvable"
                        ));

        courrierRepository.delete(courrier);
    }

    private String generateReference(
            CourrierType type
    ) {

        long total = courrierRepository.count() + 1;

        String prefix =
                type == CourrierType.ARRIVEE
                        ? "ARR"
                        : "DEP";

        return prefix
                + "-"
                + Year.now().getValue()
                + "-"
                + String.format("%03d", total);
    }

    private CourrierDTO convertToDTO(
            Courrier courrier
    ) {

        return CourrierDTO.builder()
                .id(courrier.getId())
                .reference(courrier.getReference())
                .objet(courrier.getObjet())
                .description(courrier.getDescription())
                .type(courrier.getType())
                .expediteur(courrier.getExpediteur())
                .destinataireRole(courrier.getDestinataireRole())
                .dateCourrier(courrier.getDateCourrier())
                .fichier(courrier.getFichier())
                //.createdAt(courrier.getCreatedAt())
                .build();
    }

}
