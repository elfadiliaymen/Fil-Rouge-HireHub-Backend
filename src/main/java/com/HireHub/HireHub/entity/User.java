package com.HireHub.HireHub.entity;


import com.HireHub.HireHub.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false , length = 20)
    private String nom;
    @Column(nullable = false , length = 20)
    private String prenom;
    @Column(nullable = false ,unique = true, length = 100)
    private String email;
    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean active;

    @Column(length = 20)
    private String telephone;

    @Column(length = 255)
    private String adresse;

    @Column(length = 100)
    private String entreprise;

    @Column(length = 50)
    private String poste;

    @Column(length = 20)
    private String telephonePro;

    @Column
    private LocalDate dateNaissance;

    @Column(length = 50)
    private String niveauEtude;

    @Column
    private Integer experienceAnnees;

    @Column(length = 255)
    private String linkedinUrl;


}

