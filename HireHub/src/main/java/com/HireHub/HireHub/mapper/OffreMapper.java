package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.OffreRequest;
import com.HireHub.HireHub.dto.OffreResponse;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;

public final class OffreMapper {

    private OffreMapper() {
    }

    public static OffreResponse toOffreResponse(OffreEmploi offre) {
        if (offre == null) {
            return null;
        }
        OffreResponse response = new OffreResponse();
        response.setId(offre.getId());
        response.setTitre(offre.getTitre());
        response.setDescription(offre.getDescription());
        response.setLocalisation(offre.getLocalisation());
        response.setTypeContrat(offre.getTypeContrat());
        response.setDateLimite(offre.getDateLimite());
        response.setRecruteurId(offre.getRecruteur().getId());
        response.setRecruteur(UserMapper.toUserResponse(offre.getRecruteur()));
        return response;
    }

    public static OffreEmploi toOffre(OffreRequest request, User recruteur) {
        OffreEmploi offre = new OffreEmploi();
        offre.setTitre(request.getTitre());
        offre.setDescription(request.getDescription());
        offre.setLocalisation(request.getLocalisation());
        offre.setTypeContrat(request.getTypeContrat());
        offre.setDateLimite(request.getDateLimite());
        offre.setRecruteur(recruteur);
        return offre;
    }
}
