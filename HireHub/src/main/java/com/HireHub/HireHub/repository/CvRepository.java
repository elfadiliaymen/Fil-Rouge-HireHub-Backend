package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvRepository extends JpaRepository<Cv, Long> {
    Cv findByNomFichier(String nomFichier);
}
