package com.HireHub.HireHub.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CandidatureRequest {

    @Positive(message = "Le candidat est obligatoire")
    private long candidatId;

    @Positive(message = "L'offre est obligatoire")
    private long offreId;

    private long cvId;
}