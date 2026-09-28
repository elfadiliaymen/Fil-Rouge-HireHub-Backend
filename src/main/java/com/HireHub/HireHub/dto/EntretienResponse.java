package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.StatutEntretien;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class EntretienResponse {

    private long id;
    private LocalDate date;
    private LocalTime heure;
    private String lieu;
    private StatutEntretien statut;
    private long candidatureId;
    private UserResponse recruteur;
}