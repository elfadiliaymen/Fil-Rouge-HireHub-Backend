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
        return new UserResponse(user.getId(), user.getNom(), user.getPrenom(), user.getEmail(), user.getRole(), user.isActive());
    }

    public static User toUser(UserRequest request) {
        User user = new User();
        user.setNom(request.nom());
        user.setPrenom(request.prenom());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(request.role() != null ? request.role() : Role.CANDIDAT);
        user.setActive(true);
        return user;
    }

    public static User toUser(RegisterRequest request) {
        User user = new User();
        user.setNom(request.nom());
        user.setPrenom(request.prenom());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(Role.CANDIDAT);
        user.setActive(true);
        return user;
    }

    public static OffreResponse toOffreResponse(OffreEmploi offre) {
        if (offre == null) {
            return null;
        }
        return new OffreResponse(offre.getId(), offre.getTitre(), offre.getDescription(), offre.getLocalisation(),
                offre.getTypeContrat(), offre.getDateLimite(), toUserResponse(offre.getRecruteur()));
    }

    public static OffreEmploi toOffre(OffreRequest request, User recruteur) {
        OffreEmploi offre = new OffreEmploi();
        offre.setTitre(request.titre());
        offre.setDescription(request.description());
        offre.setLocalisation(request.localisation());
        offre.setTypeContrat(request.typeContrat());
        offre.setDateLimite(request.dateLimite());
        offre.setRecruteur(recruteur);
        return offre;
    }

    public static CandidatureResponse toCandidatureResponse(Candidature candidature) {
        if (candidature == null) {
            return null;
        }
        return new CandidatureResponse(candidature.getId(), candidature.getDateCandidature(), candidature.getStatut(),
                toUserResponse(candidature.getCandidat()), toOffreResponse(candidature.getOffre()));
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
        return new EntretienResponse(entretien.getId(), entretien.getDate(), entretien.getHeure(), entretien.getLieu(),
                entretien.getCandidature().getId(), toUserResponse(entretien.getRecruteur()));
    }

    public static Entretien toEntretien(EntretienRequest request, Candidature candidature, User recruteur) {
        Entretien entretien = new Entretien();
        entretien.setDate(request.date());
        entretien.setHeure(request.heure());
        entretien.setLieu(request.lieu());
        entretien.setCandidature(candidature);
        entretien.setRecruteur(recruteur);
        return entretien;
    }

    public static CvResponse toCvResponse(Cv cv) {
        if (cv == null) {
            return null;
        }
        return new CvResponse(cv.getId(), cv.getNomFichier(), cv.getCheminFichier(), cv.getDateUpload(),
                toUserResponse(cv.getCandidat()));
    }

    public static Cv toCv(CvRequest request, User candidat) {
        Cv cv = new Cv();
        cv.setCandidat(candidat);
        cv.setNomFichier(request.nomFichier());
        cv.setCheminFichier(request.cheminFichier());
        return cv;
    }
}