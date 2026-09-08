package com.HireHub.HireHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CvResponse {

    private long id;
    private String nomFichier;
    private String cheminFichier;
    private LocalDateTime dateUpload;
    private UserResponse candidat;
}