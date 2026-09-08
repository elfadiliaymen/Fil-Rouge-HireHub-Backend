package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CandidatureRequest;
import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.dto.DTOMapper;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final OffreEmploiRepository offreEmploiRepository;

    public CandidatureService(CandidatureRepository candidatureRepository,
                              UserRepository userRepository,
                              OffreEmploiRepository offreEmploiRepository) {
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.offreEmploiRepository = offreEmploiRepository;
    }

    public CandidatureResponse consulterCandidatureParId(long candidatureId) {
        return DTOMapper.toCandidatureResponse(requerirCandidature(candidatureId));
    }

    public Page<CandidatureResponse> listerToutesLesCandidatures(Pageable pageable) {
        return candidatureRepository.findAll(pageable).map(DTOMapper::toCandidatureResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParStatut(StatutCandidature statut, Pageable pageable) {
        return candidatureRepository.findByStatut(statut, pageable).map(DTOMapper::toCandidatureResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParCandidat(long candidatId, Pageable pageable) {
        return candidatureRepository.findByCandidatId(candidatId, pageable).map(DTOMapper::toCandidatureResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParOffre(long offreId, Pageable pageable) {
        return candidatureRepository.findByOffreId(offreId, pageable).map(DTOMapper::toCandidatureResponse);
    }

    public CandidatureResponse soumettreCandidature(CandidatureRequest request) {
        User candidat = userRepository.findById(request.candidatId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidat introuvable avec l'id " + request.candidatId()));
        OffreEmploi offre = offreEmploiRepository.findById(request.offreId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable avec l'id " + request.offreId()));
        Candidature candidature = DTOMapper.toCandidature(request, candidat, offre);
        return DTOMapper.toCandidatureResponse(candidatureRepository.save(candidature));
    }

    public CandidatureResponse changerStatut(long candidatureId, StatutCandidature statut) {
        Candidature candidature = requerirCandidature(candidatureId);
        candidature.setStatut(statut);
        return DTOMapper.toCandidatureResponse(candidatureRepository.save(candidature));
    }

    public String deleteCandidature(long candidatureId) {
        candidatureRepository.deleteById(candidatureId);
        return "Candidature supprimée avec succès";
    }

    private Candidature requerirCandidature(long candidatureId) {
        return candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable avec l'id " + candidatureId));
    }
}