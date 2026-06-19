package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.CourrierNotificationDTO;
import com.tall.GestionUnchk.entity.Courrier;
import com.tall.GestionUnchk.entity.CourrierNotificaton;
import com.tall.GestionUnchk.entity.User;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.CourrierNotificatonRepository;
import com.tall.GestionUnchk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourrierNotificationService {

    private final CourrierNotificatonRepository courrierNotificatonRepository;
    private final UserRepository userRepository;

    public void envoyerNotification(
            User user,
            Courrier courrier
    ) {

        CourrierNotificaton notification =
                CourrierNotificaton.builder()
                        .user(user)
                        .courrier(courrier)
                        .titre("Nouveau courrier")
                        .message(
                                "Vous avez reçu le courrier : "
                                        + courrier.getObjet()
                        )
                        .build();

        courrierNotificatonRepository.save(notification);
    }

    public List<CourrierNotificationDTO> getMyNotifications(
            Authentication authentication
    ) {

        User user = getCurrentUser(authentication);

        return courrierNotificatonRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Long countUnread(
            Authentication authentication
    ) {

        User user = getCurrentUser(authentication);

        return courrierNotificatonRepository
                .countByUserIdAndLuFalse(user.getId());
    }

    public CourrierNotificationDTO markAsRead(
            Long notificationId,
            Authentication authentication
    ) {

        User user = getCurrentUser(authentication);

        CourrierNotificaton notification =
                courrierNotificatonRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification introuvable"
                                ));

        // Sécurité : on vérifie que la notification appartient bien à l'utilisateur connecté
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException(
                    "Notification introuvable"
            );
        }

        notification.setLu(true);

        notification = courrierNotificatonRepository.save(notification);

        return convertToDTO(notification);
    }

    private User getCurrentUser(Authentication authentication) {

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        ));
    }

    private CourrierNotificationDTO convertToDTO(
            CourrierNotificaton notification
    ) {

        return CourrierNotificationDTO.builder()
                .id(notification.getId())
                .titre(notification.getTitre())
                .message(notification.getMessage())
                .lu(notification.getLu())
                .createdAt(notification.getCreatedAt())
                .courrierId(notification.getCourrier().getId())
                .courrierReference(notification.getCourrier().getReference())
                .courrierObjet(notification.getCourrier().getObjet())
                .build();
    }
}