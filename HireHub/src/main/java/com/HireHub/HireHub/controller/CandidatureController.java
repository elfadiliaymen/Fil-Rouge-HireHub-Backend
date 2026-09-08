package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.service.CandidatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    @GetMapping
    public List<Candidature> findAll() {
        return candidatureService.listerToutesLesCandidatures();
    }

    @GetMapping("/{id}")
    public Candidature findById(@PathVariable long id) {
        return candidatureService.consulterCandidatureParId(id);
    }

    @GetMapping("/candidat/{candidatId}")
    public List<Candidature> findByCandidat(@PathVariable long candidatId) {
        return candidatureService.listerCandidaturesParCandidat(candidatId);
    }

    @GetMapping("/offre/{offreId}")
    public List<Candidature> findByOffre(@PathVariable long offreId) {
        return candidatureService.listerCandidaturesParOffre(offreId);
    }

    @GetMapping("/statut/{statut}")
    public List<Candidature> findByStatut(@PathVariable StatutCandidature statut) {
        return candidatureService.listerCandidaturesParStatut(statut);
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