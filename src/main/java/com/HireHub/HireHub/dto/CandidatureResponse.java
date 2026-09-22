package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.StatutCandidature;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CandidatureResponse {

    private long id;
    private LocalDateTime dateCandidature;
    private StatutCandidature statut;
    private UserResponse candidat;
    private OffreResponse offre;
}