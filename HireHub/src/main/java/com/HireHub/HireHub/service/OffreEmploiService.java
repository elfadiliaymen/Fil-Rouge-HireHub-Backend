package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<OffreEmploi> listerToutesLesOffres() {
        return offreEmploiRepository.findAll();
    }

    public List<OffreEmploi> listerOffresParTypeContrat(TypeContrat typeContrat) {
        return offreEmploiRepository.findByTypeContrat(typeContrat);
    }

    public List<OffreEmploi> listerOffresParLocalisation(String localisation) {
        return offreEmploiRepository.findByLocalisation(localisation);
    }

    public List<OffreEmploi> listerOffresParRecruteur(long recruteurId) {
        return offreEmploiRepository.findByRecruteurId(recruteurId);
    }

    public List<Candidature> listerCandidaturesParOffre(long offreId) {
        return candidatureRepository.findByOffreId(offreId);
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