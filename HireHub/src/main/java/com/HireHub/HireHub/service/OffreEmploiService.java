package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OffreEmploiService {

    private final OffreEmploiRepository offreEmploiRepository;
    private final CandidatureRepository candidatureRepository;

    public OffreEmploiService(OffreEmploiRepository offreEmploiRepository, CandidatureRepository candidatureRepository) {
        this.offreEmploiRepository = offreEmploiRepository;
        this.candidatureRepository = candidatureRepository;
    }

    public OffreEmploi consulterOffreParId(long offreId) {
        return offreEmploiRepository.findById(offreId).orElse(null);
    }

    public Page<OffreEmploi> listerToutesLesOffres(Pageable pageable) {
        return offreEmploiRepository.findAll(pageable);
    }

    public Page<OffreEmploi> listerOffresParTypeContrat(TypeContrat typeContrat, Pageable pageable) {
        return offreEmploiRepository.findByTypeContrat(typeContrat, pageable);
    }

    public Page<OffreEmploi> listerOffresParLocalisation(String localisation, Pageable pageable) {
        return offreEmploiRepository.findByLocalisation(localisation, pageable);
    }

    public Page<OffreEmploi> listerOffresParRecruteur(long recruteurId, Pageable pageable) {
        return offreEmploiRepository.findByRecruteurId(recruteurId, pageable);
    }

    public Page<Candidature> listerCandidaturesParOffre(long offreId, Pageable pageable) {
        return candidatureRepository.findByOffreId(offreId, pageable);
    }

    public OffreEmploi creerOffre(OffreEmploi offreEmploi) {
        return offreEmploiRepository.save(offreEmploi);
    }

    public OffreEmploi updateOffre(OffreEmploi offreEmploi) {
        return offreEmploiRepository.save(offreEmploi);
    }

    public String deleteOffre(long offreId) {
        offreEmploiRepository.deleteById(offreId);
        return "Offre supprimée avec succès";
    }
}