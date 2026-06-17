package com.tall.GestionUnchk.entity;

import com.tall.GestionUnchk.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité RoleEntity - Représente un rôle du système
 * Relation many-to-many avec User
 * Relation many-to-many avec Permission
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"permissions", "users"})
public class RoleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Role name;

    @Column(length = 255)
    private String description;

    // Relations
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Builder.Default
    private Set<PermissionEntity> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Builder.Default
    private Set<User> users = new HashSet<>();
}
