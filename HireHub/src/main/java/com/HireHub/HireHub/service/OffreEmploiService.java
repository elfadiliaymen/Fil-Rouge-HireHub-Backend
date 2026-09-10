package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.mapper.CandidatureMapper;
import com.HireHub.HireHub.mapper.OffreMapper;
import com.HireHub.HireHub.dto.OffreRequest;
import com.HireHub.HireHub.dto.OffreResponse;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
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

    public OffreEmploiService(OffreEmploiRepository offreEmploiRepository,
                              CandidatureRepository candidatureRepository,
                              UserRepository userRepository) {
        this.offreEmploiRepository = offreEmploiRepository;
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
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
        return candidatureRepository.findByOffreId(offreId, pageable).map(CandidatureMapper::toCandidatureResponse);
    }

    public OffreResponse creerOffre(OffreRequest request) {
        User recruteur = requeteUser(request.getRecruteurId());
        OffreEmploi offre = OffreMapper.toOffre(request, recruteur);
        return OffreMapper.toOffreResponse(offreEmploiRepository.save(offre));
    }

    public OffreResponse updateOffre(long offreId, OffreRequest request) {
        OffreEmploi existant = requerirOffre(offreId);
        existant.setTitre(request.getTitre());
        existant.setDescription(request.getDescription());
        existant.setLocalisation(request.getLocalisation());
        existant.setTypeContrat(request.getTypeContrat());
        existant.setDateLimite(request.getDateLimite());
        existant.setRecruteur(requeteUser(request.getRecruteurId()));
        return OffreMapper.toOffreResponse(offreEmploiRepository.save(existant));
    }

    public String deleteOffre(long offreId) {
        offreEmploiRepository.deleteById(offreId);
        return "Offre supprimée avec succès";
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