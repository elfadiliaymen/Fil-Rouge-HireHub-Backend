package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {
    List<Entretien> findByDate(LocalDate date);
    List<Entretien> findByRecruteurId(long recruteurId);
    List<Entretien> findByCandidatureId(long candidatureId);
}
