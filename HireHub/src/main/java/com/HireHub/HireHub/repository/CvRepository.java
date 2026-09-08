package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Cv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvRepository extends JpaRepository<Cv, Long> {
    Cv findByNomFichier(String nomFichier);
    Page<Cv> findByCandidatId(long candidatId, Pageable pageable);
}