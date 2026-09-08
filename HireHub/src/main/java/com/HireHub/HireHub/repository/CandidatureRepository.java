package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    Page<Candidature> findByStatut(StatutCandidature statut, Pageable pageable);
    Page<Candidature> findByCandidatId(long candidatId, Pageable pageable);
    Page<Candidature> findByOffreId(long offreId, Pageable pageable);
}