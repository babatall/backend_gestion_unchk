package com.tall.GestionUnchk.controller;

import com.tall.GestionUnchk.dto.CourrierNotificationDTO;
import com.tall.GestionUnchk.service.CourrierNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/me/notifications")
@RequiredArgsConstructor
public class CourrierNotificationController {

    private final CourrierNotificationService courrierNotificationService;

    @GetMapping
    public ResponseEntity<List<CourrierNotificationDTO>> getMyNotifications(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                courrierNotificationService.getMyNotifications(authentication)
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                courrierNotificationService.countUnread(authentication)
        );
    }

    @PatchMapping("/{id}/lu")
    public ResponseEntity<CourrierNotificationDTO> markAsRead(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                courrierNotificationService.markAsRead(id, authentication)
        );
    }
}