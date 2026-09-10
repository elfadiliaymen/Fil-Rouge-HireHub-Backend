package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.EntretienRequest;
import com.HireHub.HireHub.dto.EntretienResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.entity.User;

public final class EntretienMapper {

    private EntretienMapper() {
    }

    public static EntretienResponse toEntretienResponse(Entretien entretien) {
        if (entretien == null) {
            return null;
        }
        EntretienResponse response = new EntretienResponse();
        response.setId(entretien.getId());
        response.setDate(entretien.getDate());
        response.setHeure(entretien.getHeure());
        response.setLieu(entretien.getLieu());
        response.setCandidatureId(entretien.getCandidature().getId());
        response.setRecruteur(UserMapper.toUserResponse(entretien.getRecruteur()));
        return response;
    }

    public static Entretien toEntretien(EntretienRequest request, Candidature candidature, User recruteur) {
        Entretien entretien = new Entretien();
        entretien.setDate(request.getDate());
        entretien.setHeure(request.getHeure());
        entretien.setLieu(request.getLieu());
        entretien.setCandidature(candidature);
        entretien.setRecruteur(recruteur);
        return entretien;
    }
}
