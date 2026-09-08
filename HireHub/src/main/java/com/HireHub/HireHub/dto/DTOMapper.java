package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;

public final class DTOMapper {

    private DTOMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setActive(user.isActive());
        return response;
    }

    public static User toUser(UserRequest request) {
        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole() != null ? request.getRole() : Role.CANDIDAT);
        user.setActive(true);
        return user;
    }

    public static User toUser(RegisterRequest request) {
        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.CANDIDAT);
        user.setActive(true);
        return user;
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
        response.setRecruteur(toUserResponse(offre.getRecruteur()));
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

    public static CandidatureResponse toCandidatureResponse(Candidature candidature) {
        if (candidature == null) {
            return null;
        }
        CandidatureResponse response = new CandidatureResponse();
        response.setId(candidature.getId());
        response.setDateCandidature(candidature.getDateCandidature());
        response.setStatut(candidature.getStatut());
        response.setCandidat(toUserResponse(candidature.getCandidat()));
        response.setOffre(toOffreResponse(candidature.getOffre()));
        return response;
    }

    public static Candidature toCandidature(CandidatureRequest request, User candidat, OffreEmploi offre) {
        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        return candidature;
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
        response.setRecruteur(toUserResponse(entretien.getRecruteur()));
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

    public static CvResponse toCvResponse(Cv cv) {
        if (cv == null) {
            return null;
        }
        CvResponse response = new CvResponse();
        response.setId(cv.getId());
        response.setNomFichier(cv.getNomFichier());
        response.setCheminFichier(cv.getCheminFichier());
        response.setDateUpload(cv.getDateUpload());
        response.setCandidat(toUserResponse(cv.getCandidat()));
        return response;
    }

    public static Cv toCv(CvRequest request, User candidat) {
        Cv cv = new Cv();
        cv.setCandidat(candidat);
        cv.setNomFichier(request.getNomFichier());
        cv.setCheminFichier(request.getCheminFichier());
        return cv;
    }
}