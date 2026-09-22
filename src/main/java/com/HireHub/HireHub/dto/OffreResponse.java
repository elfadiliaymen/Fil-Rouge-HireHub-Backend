package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.TypeContrat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OffreResponse {

    private long id;
    private String titre;
    private String description;
    private String localisation;
    private TypeContrat typeContrat;
    private LocalDate dateLimite;
    private long recruteurId;
    private UserResponse recruteur;
}