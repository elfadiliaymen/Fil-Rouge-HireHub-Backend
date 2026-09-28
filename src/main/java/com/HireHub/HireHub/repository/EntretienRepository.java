package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.entity.enums.StatutEntretien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {
    Page<Entretien> findByDate(LocalDate date, Pageable pageable);
    Page<Entretien> findByDateAndRecruteurId(LocalDate date, long recruteurId, Pageable pageable);
    Page<Entretien> findByRecruteurId(long recruteurId, Pageable pageable);
    Page<Entretien> findByCandidatureId(long candidatureId, Pageable pageable);
    Page<Entretien> findByCandidatureIdAndRecruteurId(long candidatureId, long recruteurId, Pageable pageable);
    Page<Entretien> findByCandidatureCandidatId(long candidatId, Pageable pageable);
    Page<Entretien> findByDateAndCandidatureCandidatId(LocalDate date, long candidatId, Pageable pageable);
    boolean existsByCandidatureIdAndStatut(long candidatureId, StatutEntretien statut);
    void deleteByCandidatureId(long candidatureId);
    void deleteByRecruteurId(long recruteurId);
}