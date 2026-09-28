package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.mapper.CandidatureMapper;
import com.HireHub.HireHub.mapper.OffreMapper;
import com.HireHub.HireHub.dto.OffreRequest;
import com.HireHub.HireHub.dto.OffreResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.EntretienRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class OffreEmploiService {

    private final OffreEmploiRepository offreEmploiRepository;
    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final EntretienRepository entretienRepository;
    private final CurrentUserService currentUserService;

    public OffreEmploiService(OffreEmploiRepository offreEmploiRepository,
                              CandidatureRepository candidatureRepository,
                              UserRepository userRepository,
                              EntretienRepository entretienRepository,
                              CurrentUserService currentUserService) {
        this.offreEmploiRepository = offreEmploiRepository;
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.entretienRepository = entretienRepository;
        this.currentUserService = currentUserService;
    }

    public OffreResponse consulterOffreParId(long offreId) {
        return OffreMapper.toOffreResponse(requerirOffre(offreId));
    }

    public Page<OffreResponse> listerToutesLesOffres(Pageable pageable) {
        return offreEmploiRepository.findByDateLimiteGreaterThanEqual(LocalDate.now(), pageable)
                .map(OffreMapper::toOffreResponse);
    }

    public Page<OffreResponse> rechercherOffres(String motCle, TypeContrat type, Pageable pageable) {
        String motif = (motCle == null || motCle.isBlank()) ? null : motCle.trim();
        if (motif == null && type == null) {
            return listerToutesLesOffres(pageable);
        }
        return offreEmploiRepository.rechercherOffresActives(LocalDate.now(), motif, type, pageable)
                .map(OffreMapper::toOffreResponse);
    }

    public Page<OffreResponse> listerOffresParTypeContrat(TypeContrat typeContrat, Pageable pageable) {
        return offreEmploiRepository.findByTypeContrat(typeContrat, pageable).map(OffreMapper::toOffreResponse);
    }

    public Page<OffreResponse> listerOffresParLocalisation(String localisation, Pageable pageable) {
        return offreEmploiRepository.findByLocalisation(localisation, pageable).map(OffreMapper::toOffreResponse);
    }

    public Page<OffreResponse> listerOffresParRecruteur(long recruteurId, Pageable pageable) {
        interdireAuxCandidats();
        if (estRecruteurConnecte()) {
            recruteurId = currentUserService.get().getId();
        }
        return offreEmploiRepository.findByRecruteurId(recruteurId, pageable).map(OffreMapper::toOffreResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParOffre(long offreId, Pageable pageable) {
        interdireAuxCandidats();
        OffreEmploi offre = requerirOffre(offreId);
        verifierProprietaireOuAdmin(offre);
        return candidatureRepository.findByOffreId(offreId, pageable).map(CandidatureMapper::toCandidatureResponse);
    }

    public OffreResponse creerOffre(OffreRequest request) {
        interdireAuxCandidats();
        verifierDateLimite(request.getDateLimite());
        if (estRecruteurConnecte()) {
            request.setRecruteurId(currentUserService.get().getId());
        }
        User recruteur = requiererRecruteur(request.getRecruteurId());
        OffreEmploi offre = OffreMapper.toOffre(request, recruteur);
        return OffreMapper.toOffreResponse(offreEmploiRepository.save(offre));
    }

    public OffreResponse updateOffre(long offreId, OffreRequest request) {
        interdireAuxCandidats();
        verifierDateLimite(request.getDateLimite());
        OffreEmploi existant = requerirOffre(offreId);
        verifierProprietaireOuAdmin(existant);
        existant.setTitre(request.getTitre());
        existant.setDescription(request.getDescription());
        existant.setLocalisation(request.getLocalisation());
        existant.setTypeContrat(request.getTypeContrat());
        existant.setDateLimite(request.getDateLimite());
        if (estRecruteurConnecte()) {
            request.setRecruteurId(currentUserService.get().getId());
        }
        existant.setRecruteur(requiererRecruteur(request.getRecruteurId()));
        return OffreMapper.toOffreResponse(offreEmploiRepository.save(existant));
    }

    public String deleteOffre(long offreId) {
        interdireAuxCandidats();
        OffreEmploi existant = requerirOffre(offreId);
        verifierProprietaireOuAdmin(existant);
        List<Candidature> candidatures = candidatureRepository.findByOffreId(offreId);
        for (Candidature candidature : candidatures) {
            entretienRepository.deleteByCandidatureId(candidature.getId());
            candidatureRepository.delete(candidature);
        }
        offreEmploiRepository.deleteById(offreId);
        return "Offre supprimée avec succès";
    }

    private boolean estRecruteurConnecte() {
        return currentUserService.hasRole(Role.RECRUTEUR);
    }

    private void interdireAuxCandidats() {
        if (currentUserService.isCandidat()) {
            throw new ResourceNotFoundException("Offre introuvable avec l'id 0");
        }
    }

    private void verifierDateLimite(LocalDate dateLimite) {
        if (dateLimite == null || dateLimite.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date limite doit être aujourd'hui ou dans le futur");
        }
    }

    private void verifierProprietaireOuAdmin(OffreEmploi offre) {
        if (estRecruteurConnecte()
                && offre.getRecruteur().getId() != currentUserService.get().getId()) {
            throw new ResourceNotFoundException("Offre introuvable avec l'id " + offre.getId());
        }
    }

    private OffreEmploi requerirOffre(long offreId) {
        return offreEmploiRepository.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable avec l'id " + offreId));
    }

    private User requeteUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id " + userId));
    }

    private User requiererRecruteur(long userId) {
        User recruteur = requeteUser(userId);
        if (recruteur.getRole() != Role.RECRUTEUR) {
            throw new IllegalArgumentException(
                    "L'offre doit être assignée à un utilisateur ayant le rôle RECRUTEUR");
        }
        return recruteur;
    }
}