package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.OffreRequest;
import com.HireHub.HireHub.dto.OffreResponse;
import com.HireHub.HireHub.dto.UserPublicResponse;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;

import java.time.LocalDate;

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
        response.setDatePublication(offre.getDatePublication());
        response.setRecruteurId(offre.getRecruteur().getId());
        response.setRecruteur(toUserPublicResponse(offre.getRecruteur()));
        return response;
    }

    public static UserPublicResponse toUserPublicResponse(User user) {
        if (user == null) {
            return null;
        }
        UserPublicResponse response = new UserPublicResponse();
        response.setId(user.getId());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setEntreprise(user.getEntreprise());
        response.setPoste(user.getPoste());
        return response;
    }

    public static OffreEmploi toOffre(OffreRequest request, User recruteur) {
        OffreEmploi offre = new OffreEmploi();
        offre.setTitre(request.getTitre());
        offre.setDescription(request.getDescription());
        offre.setLocalisation(request.getLocalisation());
        offre.setTypeContrat(request.getTypeContrat());
        offre.setDateLimite(request.getDateLimite());
        offre.setDatePublication(LocalDate.now());
        offre.setRecruteur(recruteur);
        return offre;
    }
}
