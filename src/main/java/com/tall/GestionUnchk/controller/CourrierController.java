package com.tall.GestionUnchk.controller;

import com.tall.GestionUnchk.dto.CourrierCreateUpdateDTO;
import com.tall.GestionUnchk.dto.CourrierDTO;
import com.tall.GestionUnchk.service.CourrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courriers")
@RequiredArgsConstructor
public class CourrierController {


    private final CourrierService courrierService;

    @GetMapping
    public ResponseEntity<List<CourrierDTO>> getAll() {

        return ResponseEntity.ok(
                courrierService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourrierDTO> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                courrierService.getById(id)
        );
    }

    @PostMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','PERSONNEL_ADMINISTRATIF')"
    )
    public ResponseEntity<CourrierDTO> create(
            @RequestBody CourrierCreateUpdateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courrierService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','PERSONNEL_ADMINISTRATIF')"
    )
    public ResponseEntity<CourrierDTO> update(
            @PathVariable Long id,
            @RequestBody CourrierCreateUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                courrierService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        courrierService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
