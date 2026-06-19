package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.Courrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourrierRepository
        extends JpaRepository<Courrier, Long> {
}
