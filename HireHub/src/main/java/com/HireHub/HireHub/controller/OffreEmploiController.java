package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.dto.OffreRequest;
import com.HireHub.HireHub.dto.OffreResponse;
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
    public Page<OffreResponse> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerToutesLesOffres(pageable);
    }

    @GetMapping("/{id}")
    public OffreResponse findById(@PathVariable long id) {
        return offreEmploiService.consulterOffreParId(id);
    }

    @GetMapping("/type/{typeContrat}")
    public Page<OffreResponse> findByTypeContrat(@PathVariable TypeContrat typeContrat,
                                                 @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerOffresParTypeContrat(typeContrat, pageable);
    }

    @GetMapping("/localisation/{localisation}")
    public Page<OffreResponse> findByLocalisation(@PathVariable String localisation,
                                                  @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerOffresParLocalisation(localisation, pageable);
    }

    @GetMapping("/recruteur/{recruteurId}")
    public Page<OffreResponse> findByRecruteur(@PathVariable long recruteurId,
                                               @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerOffresParRecruteur(recruteurId, pageable);
    }

    @GetMapping("/{id}/candidatures")
    public Page<CandidatureResponse> candidaturesParOffre(@PathVariable long id,
                                                          @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return offreEmploiService.listerCandidaturesParOffre(id, pageable);
    }

    @PostMapping
    public ResponseEntity<OffreResponse> save(@RequestBody OffreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offreEmploiService.creerOffre(request));
    }

    @PutMapping("/{id}")
    public OffreResponse update(@PathVariable long id, @RequestBody OffreRequest request) {
        return offreEmploiService.updateOffre(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return offreEmploiService.deleteOffre(id);
    }
}