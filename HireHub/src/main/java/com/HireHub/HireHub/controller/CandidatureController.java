package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.Candidature;
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
    public Page<Candidature> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerToutesLesCandidatures(pageable);
    }

    @GetMapping("/{id}")
    public Candidature findById(@PathVariable long id) {
        return candidatureService.consulterCandidatureParId(id);
    }

    @GetMapping("/candidat/{candidatId}")
    public Page<Candidature> findByCandidat(@PathVariable long candidatId,
                                            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerCandidaturesParCandidat(candidatId, pageable);
    }

    @GetMapping("/offre/{offreId}")
    public Page<Candidature> findByOffre(@PathVariable long offreId,
                                         @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerCandidaturesParOffre(offreId, pageable);
    }

    @GetMapping("/statut/{statut}")
    public Page<Candidature> findByStatut(@PathVariable StatutCandidature statut,
                                          @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidatureService.listerCandidaturesParStatut(statut, pageable);
    }

    @PostMapping
    public ResponseEntity<Candidature> save(@RequestBody Candidature candidature) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatureService.soumettreCandidature(candidature));
    }

    @PutMapping
    public ResponseEntity<Candidature> update(@RequestBody Candidature candidature) {
        return ResponseEntity.ok(candidatureService.updateCandidature(candidature));
    }

    @PatchMapping("/{id}/statut/{statut}")
    public ResponseEntity<Candidature> changerStatut(@PathVariable long id, @PathVariable StatutCandidature statut) {
        Candidature miseAJour = candidatureService.changerStatut(id, statut);
        if (miseAJour == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(miseAJour);
    }

    @PatchMapping("/{id}/accepter")
    public ResponseEntity<Candidature> accepter(@PathVariable long id) {
        return ResponseEntity.ok(candidatureService.changerStatut(id, StatutCandidature.ACCEPTEE));
    }

    @PatchMapping("/{id}/refuser")
    public ResponseEntity<Candidature> refuser(@PathVariable long id) {
        return ResponseEntity.ok(candidatureService.changerStatut(id, StatutCandidature.REFUSEE));
    }

    @PatchMapping("/{id}/en-attente")
    public ResponseEntity<Candidature> enAttente(@PathVariable long id) {
        return ResponseEntity.ok(candidatureService.changerStatut(id, StatutCandidature.EN_ATTENTE));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return candidatureService.deleteCandidature(id);
    }
}