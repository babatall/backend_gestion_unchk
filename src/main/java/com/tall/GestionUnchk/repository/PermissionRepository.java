package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.PermissionEntity;
import com.tall.GestionUnchk.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité PermissionEntity
 */
@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    
    Optional<PermissionEntity> findByName(Permission name);
}
