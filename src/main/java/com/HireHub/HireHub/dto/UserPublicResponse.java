package com.HireHub.HireHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPublicResponse {

    private long id;
    private String nom;
    private String prenom;
    private String entreprise;
    private String poste;
}