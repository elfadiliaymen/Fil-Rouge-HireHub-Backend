package com.HireHub.HireHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CvFichier {

    private String nomFichier;
    private byte[] contenu;
}