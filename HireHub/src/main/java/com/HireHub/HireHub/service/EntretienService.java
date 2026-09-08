package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.repository.EntretienRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EntretienService {

    private final EntretienRepository entretienRepository;

    public EntretienService(EntretienRepository entretienRepository) {
        this.entretienRepository = entretienRepository;
    }

    public Entretien consulterEntretienParId(long entretienId) {
        return entretienRepository.findById(entretienId).orElse(null);
    }

    public Page<Entretien> listerTousLesEntretiens(Pageable pageable) {
        return entretienRepository.findAll(pageable);
    }

    public Page<Entretien> listerEntretiensParDate(LocalDate date, Pageable pageable) {
        return entretienRepository.findByDate(date, pageable);
    }

    public Page<Entretien> listerEntretiensParRecruteur(long recruteurId, Pageable pageable) {
        return entretienRepository.findByRecruteurId(recruteurId, pageable);
    }

    public Page<Entretien> listerEntretiensParCandidature(long candidatureId, Pageable pageable) {
        return entretienRepository.findByCandidatureId(candidatureId, pageable);
    }

    public Entretien planifierEntretien(Entretien entretien) {
        return entretienRepository.save(entretien);
    }

    public Entretien updateEntretien(Entretien entretien) {
        return entretienRepository.save(entretien);
    }

    public String deleteEntretien(long entretienId) {
        entretienRepository.deleteById(entretienId);
        return "Entretien supprimé avec succès";
    }
}