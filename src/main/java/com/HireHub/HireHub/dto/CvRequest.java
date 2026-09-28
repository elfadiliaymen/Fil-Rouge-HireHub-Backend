package com.HireHub.HireHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CvRequest {

    @Positive(message = "Le candidat est obligatoire")
    private long candidatId;

    @NotBlank(message = "Le nom du fichier est obligatoire")
    private String nomFichier;

    @NotBlank(message = "Le chemin du fichier est obligatoire")
    private String cheminFichier;
}