package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Entretien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {
    Page<Entretien> findByDate(LocalDate date, Pageable pageable);
    Page<Entretien> findByRecruteurId(long recruteurId, Pageable pageable);
    Page<Entretien> findByCandidatureId(long candidatureId, Pageable pageable);
}