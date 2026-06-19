package com.tall.GestionUnchk.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "administrative_staff")
public class AdministrativeStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeNumber;

    private String department;

    private String position;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
