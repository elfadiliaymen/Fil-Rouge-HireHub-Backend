package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.repository.EntretienRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EntretienService {

    private final EntretienRepository entretienRepository;

    public EntretienService(EntretienRepository entretienRepository) {
        this.entretienRepository = entretienRepository;
    }

    public Entretien consulterEntretienParId(long entretienId) {
        return entretienRepository.findById(entretienId).orElse(null);
    }

    public List<Entretien> listerTousLesEntretiens() {
        return entretienRepository.findAll();
    }

    public List<Entretien> listerEntretiensParDate(LocalDate date) {
        return entretienRepository.findByDate(date);
    }

    public Entretien planifierEntretien(Entretien entretien) {
        return entretienRepository.save(entretien);
    }

    public Entretien updateEntretien(Entretien entretien) {
        return entretienRepository.save(entretien);
    }

    public String deleteEntretien(long entretienId) {
        entretienRepository.deleteById(entretienId);
        return "Entretien deleted successfully";
    }
}
