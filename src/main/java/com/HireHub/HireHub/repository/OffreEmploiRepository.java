package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Long> {
    Page<OffreEmploi> findByTypeContrat(TypeContrat typeContrat, Pageable pageable);
    Page<OffreEmploi> findByLocalisation(String localisation, Pageable pageable);
    Page<OffreEmploi> findByRecruteurId(long recruteurId, Pageable pageable);
    List<OffreEmploi> findByRecruteurId(long recruteurId);
    Page<OffreEmploi> findByDateLimiteGreaterThanEqual(LocalDate date, Pageable pageable);

    @Query("""
            select offre from OffreEmploi offre
            where offre.dateLimite >= :today
              and (:type is null or offre.typeContrat = :type)
              and (:motCle is null
                   or lower(offre.titre) like lower(concat('%', :motCle, '%'))
                   or lower(offre.localisation) like lower(concat('%', :motCle, '%'))
                   or lower(offre.description) like lower(concat('%', :motCle, '%')))
            """)
    Page<OffreEmploi> rechercherOffresActives(@Param("today") LocalDate today,
                                              @Param("motCle") String motCle,
                                              @Param("type") TypeContrat type,
                                              Pageable pageable);
}