package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.dto.CandidatureRequest;
import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.service.CandidatureService;
import com.HireHub.HireHub.service.CurrentUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    private final CandidatureService candidatureService;
    private final CurrentUserService currentUserService;

    public CandidatureController(CandidatureService candidatureService,
                                 CurrentUserService currentUserService) {
        this.candidatureService = candidatureService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public Page<CandidatureResponse> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        if (currentUserService.isCandidat()) {
            return candidatureService.listerCandidaturesParCandidat(currentUserService.get().getId(), pageable);
        }
        if (currentUserService.isRecruteur()) {
            return candidatureService.listerCandidaturesParRecruteur(currentUserService.get().getId(), pageable);
        }
        return candidatureService.listerToutesLesCandidatures(pageable);
    }

    @GetMapping("/{id}")
    public CandidatureResponse findById(@PathVariable long id) {
        CandidatureResponse candidature = candidatureService.consulterCandidatureParId(id);

        if (currentUserService.isCandidat()
                && candidature.getCandidat().getId() != currentUserService.get().getId()) {
            throw new com.HireHub.HireHub.exception.ResourceNotFoundException(
                    "Candidature introuvable avec l'id " + id);
        }

        return candidature;
    }

    @GetMapping("/candidat/{candidatId}")
    public Page<CandidatureResponse> findByCandidat(@PathVariable long candidatId,
                                                    @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        if (currentUserService.isCandidat()) {
            candidatId = currentUserService.get().getId();
        }
        return candidatureService.listerCandidaturesParCandidat(candidatId, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    @GetMapping("/offre/{offreId}")
    public Page<CandidatureResponse> findByOffre(@PathVariable long offreId,
                                                 @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerCandidaturesParOffre(offreId, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    @GetMapping("/statut/{statut}")
    public Page<CandidatureResponse> findByStatut(@PathVariable StatutCandidature statut,
                                                  @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerCandidaturesParStatut(statut, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    @PostMapping
    public ResponseEntity<CandidatureResponse> save(@RequestBody CandidatureRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatureService.soumettreCandidature(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    @PatchMapping("/{id}/statut/{statut}")
    public CandidatureResponse changerStatut(@PathVariable long id, @PathVariable StatutCandidature statut) {
        return candidatureService.changerStatut(id, statut);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    @PatchMapping("/{id}/accepter")
    public CandidatureResponse accepter(@PathVariable long id) {
        return candidatureService.changerStatut(id, StatutCandidature.ACCEPTEE);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    @PatchMapping("/{id}/refuser")
    public CandidatureResponse refuser(@PathVariable long id) {
        return candidatureService.changerStatut(id, StatutCandidature.REFUSEE);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    @PatchMapping("/{id}/en-attente")
    public CandidatureResponse enAttente(@PathVariable long id) {
        return candidatureService.changerStatut(id, StatutCandidature.EN_ATTENTE);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return candidatureService.deleteCandidature(id);
    }
}