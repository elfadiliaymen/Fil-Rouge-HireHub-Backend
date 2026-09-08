package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.dto.CandidatureRequest;
import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.service.CandidatureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    @GetMapping
    public Page<CandidatureResponse> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerToutesLesCandidatures(pageable);
    }

    @GetMapping("/{id}")
    public CandidatureResponse findById(@PathVariable long id) {
        return candidatureService.consulterCandidatureParId(id);
    }

    @GetMapping("/candidat/{candidatId}")
    public Page<CandidatureResponse> findByCandidat(@PathVariable long candidatId,
                                                    @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerCandidaturesParCandidat(candidatId, pageable);
    }

    @GetMapping("/offre/{offreId}")
    public Page<CandidatureResponse> findByOffre(@PathVariable long offreId,
                                                 @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerCandidaturesParOffre(offreId, pageable);
    }

    @GetMapping("/statut/{statut}")
    public Page<CandidatureResponse> findByStatut(@PathVariable StatutCandidature statut,
                                                  @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerCandidaturesParStatut(statut, pageable);
    }

    @PostMapping
    public ResponseEntity<CandidatureResponse> save(@RequestBody CandidatureRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatureService.soumettreCandidature(request));
    }

    @PatchMapping("/{id}/statut/{statut}")
    public CandidatureResponse changerStatut(@PathVariable long id, @PathVariable StatutCandidature statut) {
        return candidatureService.changerStatut(id, statut);
    }

    @PatchMapping("/{id}/accepter")
    public CandidatureResponse accepter(@PathVariable long id) {
        return candidatureService.changerStatut(id, StatutCandidature.ACCEPTEE);
    }

    @PatchMapping("/{id}/refuser")
    public CandidatureResponse refuser(@PathVariable long id) {
        return candidatureService.changerStatut(id, StatutCandidature.REFUSEE);
    }

    @PatchMapping("/{id}/en-attente")
    public CandidatureResponse enAttente(@PathVariable long id) {
        return candidatureService.changerStatut(id, StatutCandidature.EN_ATTENTE);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return candidatureService.deleteCandidature(id);
    }
}