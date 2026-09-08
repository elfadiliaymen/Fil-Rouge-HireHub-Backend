package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CvRepository extends JpaRepository<Cv, Long> {
    Cv findByNomFichier(String nomFichier);
    List<Cv> findByCandidatId(long candidatId);
}