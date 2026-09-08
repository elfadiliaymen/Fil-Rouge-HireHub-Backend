package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Long> {
    Page<OffreEmploi> findByTypeContrat(TypeContrat typeContrat, Pageable pageable);
    Page<OffreEmploi> findByLocalisation(String localisation, Pageable pageable);
    Page<OffreEmploi> findByRecruteurId(long recruteurId, Pageable pageable);
}