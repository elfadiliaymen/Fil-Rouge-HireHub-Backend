package com.HireHub.HireHub.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EntretienResponse(
        long id,
        LocalDate date,
        LocalTime heure,
        String lieu,
        long candidatureId,
        UserResponse recruteur) {
}