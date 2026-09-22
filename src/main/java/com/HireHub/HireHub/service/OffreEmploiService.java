package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.mapper.CandidatureMapper;
import com.HireHub.HireHub.mapper.OffreMapper;
import com.HireHub.HireHub.dto.OffreRequest;
import com.HireHub.HireHub.dto.OffreResponse;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OffreEmploiService {

    private final OffreEmploiRepository offreEmploiRepository;
    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public OffreEmploiService(OffreEmploiRepository offreEmploiRepository,
                              CandidatureRepository candidatureRepository,
                              UserRepository userRepository,
                              CurrentUserService currentUserService) {
        this.offreEmploiRepository = offreEmploiRepository;
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public OffreResponse consulterOffreParId(long offreId) {
        return OffreMapper.toOffreResponse(requerirOffre(offreId));
    }

    public Page<OffreResponse> listerToutesLesOffres(Pageable pageable) {
        return offreEmploiRepository.findAll(pageable).map(OffreMapper::toOffreResponse);
    }

    public Page<OffreResponse> listerOffresParTypeContrat(TypeContrat typeContrat, Pageable pageable) {
        return offreEmploiRepository.findByTypeContrat(typeContrat, pageable).map(OffreMapper::toOffreResponse);
    }

    public Page<OffreResponse> listerOffresParLocalisation(String localisation, Pageable pageable) {
        return offreEmploiRepository.findByLocalisation(localisation, pageable).map(OffreMapper::toOffreResponse);
    }

    public Page<OffreResponse> listerOffresParRecruteur(long recruteurId, Pageable pageable) {
        return offreEmploiRepository.findByRecruteurId(recruteurId, pageable).map(OffreMapper::toOffreResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParOffre(long offreId, Pageable pageable) {
        OffreEmploi offre = requerirOffre(offreId);
        verifierProprietaireOuAdmin(offre);
        return candidatureRepository.findByOffreId(offreId, pageable).map(CandidatureMapper::toCandidatureResponse);
    }

    public OffreResponse creerOffre(OffreRequest request) {
        if (estRecruteurConnecte()) {
            request.setRecruteurId(currentUserService.get().getId());
        }
        User recruteur = requeteUser(request.getRecruteurId());
        OffreEmploi offre = OffreMapper.toOffre(request, recruteur);
        return OffreMapper.toOffreResponse(offreEmploiRepository.save(offre));
    }

    public OffreResponse updateOffre(long offreId, OffreRequest request) {
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
        existant.setRecruteur(requeteUser(request.getRecruteurId()));
        return OffreMapper.toOffreResponse(offreEmploiRepository.save(existant));
    }

    public String deleteOffre(long offreId) {
        OffreEmploi existant = requerirOffre(offreId);
        verifierProprietaireOuAdmin(existant);
        offreEmploiRepository.deleteById(offreId);
        return "Offre supprimée avec succès";
    }

    private boolean estRecruteurConnecte() {
        return currentUserService.hasRole(Role.RECRUTEUR);
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
}