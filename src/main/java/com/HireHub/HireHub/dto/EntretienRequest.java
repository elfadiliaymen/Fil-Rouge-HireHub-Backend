package com.HireHub.HireHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class EntretienRequest {

    @NotNull(message = "La date est obligatoire")
    private LocalDate date;

    @NotNull(message = "L'heure est obligatoire")
    private LocalTime heure;

    @NotBlank(message = "Le lieu est obligatoire")
    @Size(max = 150, message = "Le lieu ne doit pas dépasser 150 caractères")
    private String lieu;

    @Positive(message = "La candidature est obligatoire")
    private long candidatureId;

    @Positive(message = "Le recruteur est obligatoire")
    private long recruteurId;
}