package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 20, message = "Le nom ne doit pas dépasser 20 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 20, message = "Le prénom ne doit pas dépasser 20 caractères")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, max = 255, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    @Size(max = 20, message = "Le numéro ne doit pas dépasser 20 caractères")
    private String telephone;

    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String adresse;

    @Size(max = 100, message = "L'entreprise ne doit pas dépasser 100 caractères")
    private String entreprise;

    @Size(max = 50, message = "Le poste ne doit pas dépasser 50 caractères")
    private String poste;

    @Size(max = 20, message = "Le numéro ne doit pas dépasser 20 caractères")
    private String telephonePro;

    private LocalDate dateNaissance;

    @Size(max = 50, message = "Le niveau d'étude ne doit pas dépasser 50 caractères")
    private String niveauEtude;

    @Min(value = 0, message = "Le nombre d'années d'expérience ne peut pas être négatif")
    private Integer experienceAnnees;

    @Size(max = 255, message = "Le lien LinkedIn ne doit pas dépasser 255 caractères")
    private String linkedinUrl;
}