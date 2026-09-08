package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.StatutCandidature;

import java.time.LocalDateTime;

public record CandidatureResponse(
        long id,
        LocalDateTime dateCandidature,
        StatutCandidature statut,
        UserResponse candidat,
        OffreResponse offre) {
}