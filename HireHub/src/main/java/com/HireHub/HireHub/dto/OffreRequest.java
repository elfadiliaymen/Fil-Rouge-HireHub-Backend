package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.TypeContrat;

import java.time.LocalDate;

public record OffreRequest(
        String titre,
        String description,
        String localisation,
        TypeContrat typeContrat,
        LocalDate dateLimite,
        long recruteurId) {
}