package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Long> {
    List<OffreEmploi> findByTypeContrat(TypeContrat typeContrat);
    List<OffreEmploi> findByLocalisation(String localisation);
    List<OffreEmploi> findByRecruteurId(long recruteurId);
}
