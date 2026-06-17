package com.tall.GestionUnchk.entity;

import com.tall.GestionUnchk.enums.Permission;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité PermissionEntity - Représente une permission du système
 * Relation many-to-many avec Role
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"roles"})
public class PermissionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Permission name;

    @Column(length = 255)
    private String description;

    // Relations
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();
}
