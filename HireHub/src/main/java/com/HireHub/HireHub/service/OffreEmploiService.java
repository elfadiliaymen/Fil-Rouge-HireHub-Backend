package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffreEmploiService {

    private final OffreEmploiRepository offreEmploiRepository;

    public OffreEmploiService(OffreEmploiRepository offreEmploiRepository) {
        this.offreEmploiRepository = offreEmploiRepository;
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

    public OffreEmploi creerOffre(OffreEmploi offreEmploi) {
        return offreEmploiRepository.save(offreEmploi);
    }

    public OffreEmploi updateOffre(OffreEmploi offreEmploi) {
        return offreEmploiRepository.save(offreEmploi);
    }

    public String deleteOffre(long offreId) {
        offreEmploiRepository.deleteById(offreId);
        return "OffreEmploi deleted successfully";
    }
}
