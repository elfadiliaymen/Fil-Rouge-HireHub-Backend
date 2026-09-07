package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.repository.CandidatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;

    public CandidatureService(CandidatureRepository candidatureRepository) {
        this.candidatureRepository = candidatureRepository;
    }

    public Candidature consulterCandidatureParId(long candidatureId) {
        return candidatureRepository.findById(candidatureId).orElse(null);
    }

    public List<Candidature> listerToutesLesCandidatures() {
        return candidatureRepository.findAll();
    }

    public List<Candidature> listerCandidaturesParStatut(StatutCandidature statut) {
        return candidatureRepository.findByStatut(statut);
    }

    public Candidature soumettreCandidature(Candidature candidature) {
        return candidatureRepository.save(candidature);
    }

    public Candidature updateCandidature(Candidature candidature) {
        return candidatureRepository.save(candidature);
    }

    public String deleteCandidature(long candidatureId) {
        candidatureRepository.deleteById(candidatureId);
        return "Candidature deleted successfully";
    }
}
