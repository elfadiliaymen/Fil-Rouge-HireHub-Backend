package com.HireHub.HireHub.dto;

import java.time.LocalDateTime;

public record CvResponse(
        long id,
        String nomFichier,
        String cheminFichier,
        LocalDateTime dateUpload,
        UserResponse candidat) {
}