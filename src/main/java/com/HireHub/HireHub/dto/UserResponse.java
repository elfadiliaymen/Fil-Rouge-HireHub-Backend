package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserResponse {

    private long id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private boolean active;

    private String telephone;
    private String adresse;

    private String entreprise;
    private String poste;
    private String telephonePro;

    private LocalDate dateNaissance;
    private String niveauEtude;
    private int experienceAnnees;
    private String linkedinUrl;
}