package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.repository.CandidatureRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;

    public CandidatureService(CandidatureRepository candidatureRepository) {
        this.candidatureRepository = candidatureRepository;
    }

    public Candidature consulterCandidatureParId(long candidatureId) {
        return candidatureRepository.findById(candidatureId).orElse(null);
    }

    public Page<Candidature> listerToutesLesCandidatures(Pageable pageable) {
        return candidatureRepository.findAll(pageable);
    }

    public Page<Candidature> listerCandidaturesParStatut(StatutCandidature statut, Pageable pageable) {
        return candidatureRepository.findByStatut(statut, pageable);
    }

    public Page<Candidature> listerCandidaturesParCandidat(long candidatId, Pageable pageable) {
        return candidatureRepository.findByCandidatId(candidatId, pageable);
    }

    public Page<Candidature> listerCandidaturesParOffre(long offreId, Pageable pageable) {
        return candidatureRepository.findByOffreId(offreId, pageable);
    }

    public Candidature soumettreCandidature(Candidature candidature) {
        return candidatureRepository.save(candidature);
    }

    public Candidature updateCandidature(Candidature candidature) {
        return candidatureRepository.save(candidature);
    }

    public Candidature changerStatut(long candidatureId, StatutCandidature statut) {
        Candidature candidature = consulterCandidatureParId(candidatureId);
        if (candidature == null) {
            return null;
        }
        candidature.setStatut(statut);
        return candidatureRepository.save(candidature);
    }

    public String deleteCandidature(long candidatureId) {
        candidatureRepository.deleteById(candidatureId);
        return "Candidature supprimée avec succès";
    }
}