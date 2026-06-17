package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.RoleEntity;
import com.tall.GestionUnchk.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité RoleEntity
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    
    Optional<RoleEntity> findByName(Role name);
}
