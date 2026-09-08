package com.HireHub.HireHub.dto;

public record CvRequest(
        long candidatId,
        String nomFichier,
        String cheminFichier) {
}