package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByStatut(StatutCandidature statut);
    List<Candidature> findByCandidatId(long candidatId);
    List<Candidature> findByOffreId(long offreId);
}
