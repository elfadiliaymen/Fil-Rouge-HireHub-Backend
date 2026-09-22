package com.HireHub.HireHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UpdateProfileRequest {

    private String nom;
    private String prenom;

    private String telephone;
    private String adresse;

    private String entreprise;
    private String poste;
    private String telephonePro;

    private LocalDate dateNaissance;
    private String niveauEtude;
    private Integer experienceAnnees;
    private String linkedinUrl;
}