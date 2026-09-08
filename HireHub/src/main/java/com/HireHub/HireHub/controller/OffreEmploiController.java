package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import com.HireHub.HireHub.service.OffreEmploiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offres")
public class OffreEmploiController {

    private final OffreEmploiService offreEmploiService;

    public OffreEmploiController(OffreEmploiService offreEmploiService) {
        this.offreEmploiService = offreEmploiService;
    }

    @GetMapping
    public Page<OffreEmploi> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerToutesLesOffres(pageable);
    }

    @GetMapping("/{id}")
    public OffreEmploi findById(@PathVariable long id) {
        return offreEmploiService.consulterOffreParId(id);
    }

    @GetMapping("/type/{typeContrat}")
    public Page<OffreEmploi> findByTypeContrat(@PathVariable TypeContrat typeContrat,
                                               @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerOffresParTypeContrat(typeContrat, pageable);
    }

    @GetMapping("/localisation/{localisation}")
    public Page<OffreEmploi> findByLocalisation(@PathVariable String localisation,
                                                @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerOffresParLocalisation(localisation, pageable);
    }

    @GetMapping("/recruteur/{recruteurId}")
    public Page<OffreEmploi> findByRecruteur(@PathVariable long recruteurId,
                                             @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerOffresParRecruteur(recruteurId, pageable);
    }

    @GetMapping("/{id}/candidatures")
    public Page<Candidature> candidaturesParOffre(@PathVariable long id,
                                                  @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerCandidaturesParOffre(id, pageable);
    }

    @PostMapping
    public ResponseEntity<OffreEmploi> save(@RequestBody OffreEmploi offreEmploi) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offreEmploiService.creerOffre(offreEmploi));
    }

    @PutMapping
    public ResponseEntity<OffreEmploi> update(@RequestBody OffreEmploi offreEmploi) {
        return ResponseEntity.ok(offreEmploiService.updateOffre(offreEmploi));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return offreEmploiService.deleteOffre(id);
    }
}