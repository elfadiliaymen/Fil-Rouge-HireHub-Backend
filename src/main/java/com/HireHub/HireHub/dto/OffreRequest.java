package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.TypeContrat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OffreRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 150, message = "Le titre ne doit pas dépasser 150 caractères")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotBlank(message = "La localisation est obligatoire")
    @Size(max = 150, message = "La localisation ne doit pas dépasser 150 caractères")
    private String localisation;

    @NotNull(message = "Le type de contrat est obligatoire")
    private TypeContrat typeContrat;

    @NotNull(message = "La date limite est obligatoire")
    private LocalDate dateLimite;

    @Positive(message = "Le recruteur est obligatoire")
    private long recruteurId;
}