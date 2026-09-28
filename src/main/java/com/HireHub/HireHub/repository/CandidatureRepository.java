package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    Page<Candidature> findByStatut(StatutCandidature statut, Pageable pageable);
    Page<Candidature> findByStatutAndCandidatId(StatutCandidature statut, long candidatId, Pageable pageable);
    Page<Candidature> findByStatutAndOffreRecruteurId(StatutCandidature statut, long recruteurId, Pageable pageable);
    Page<Candidature> findByCandidatId(long candidatId, Pageable pageable);
    List<Candidature> findByCandidatId(long candidatId);
    Page<Candidature> findByCandidatIdAndOffreRecruteurId(long candidatId, long recruteurId, Pageable pageable);
    Page<Candidature> findByOffreRecruteurId(long recruteurId, Pageable pageable);
    Page<Candidature> findByOffreId(long offreId, Pageable pageable);
    List<Candidature> findByOffreId(long offreId);
    boolean existsByCandidatIdAndOffreId(long candidatId, long offreId);
    boolean existsByCv_IdAndOffre_Recruteur_Id(long cvId, long recruteurId);
    long countByCandidatId(long candidatId);
    long countByCandidatIdAndStatut(long candidatId, StatutCandidature statut);

    @Modifying
    @Query("update Candidature candidature set candidature.cv = null where candidature.cv.id = :cvId")
    void detacherCv(@Param("cvId") long cvId);
}