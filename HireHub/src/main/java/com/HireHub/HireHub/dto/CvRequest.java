package com.HireHub.HireHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CvRequest {

    private long candidatId;
    private String nomFichier;
    private String cheminFichier;
}