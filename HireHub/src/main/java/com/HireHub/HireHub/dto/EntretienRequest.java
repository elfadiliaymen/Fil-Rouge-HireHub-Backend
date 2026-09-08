package com.HireHub.HireHub.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EntretienRequest(
        LocalDate date,
        LocalTime heure,
        String lieu,
        long candidatureId,
        long recruteurId) {
}