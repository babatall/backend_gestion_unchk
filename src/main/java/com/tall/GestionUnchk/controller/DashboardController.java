package com.tall.GestionUnchk.controller;

import com.tall.GestionUnchk.dto.DashboardStatsDTO;
import com.tall.GestionUnchk.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() {

        return ResponseEntity.ok(
                dashboardService.getStats()
        );
    }
}