package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.TypeContrat;

import java.time.LocalDate;

public record OffreResponse(
        long id,
        String titre,
        String description,
        String localisation,
        TypeContrat typeContrat,
        LocalDate dateLimite,
        UserResponse recruteur) {
}