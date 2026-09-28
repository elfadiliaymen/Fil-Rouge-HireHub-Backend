package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.Cv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CvRepository extends JpaRepository<Cv, Long> {
    Page<Cv> findByCandidatId(long candidatId, Pageable pageable);
    void deleteByCandidatId(long candidatId);

    @Query("select distinct cv from Cv cv join Candidature cand on cand.cv = cv where cand.offre.recruteur.id = :recruteurId")
    Page<Cv> findByCandidaturesOffreRecruteurId(@Param("recruteurId") long recruteurId, Pageable pageable);

    @Query("select distinct cv from Cv cv join Candidature cand on cand.cv = cv where cand.candidat.id = :candidatId and cand.offre.recruteur.id = :recruteurId")
    Page<Cv> findByCandidatIdAndCandidaturesOffreRecruteurId(@Param("candidatId") long candidatId,
                                                             @Param("recruteurId") long recruteurId,
                                                             Pageable pageable);
}