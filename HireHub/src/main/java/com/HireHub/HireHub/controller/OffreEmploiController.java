package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.enums.TypeContrat;
import com.HireHub.HireHub.service.OffreEmploiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
public class OffreEmploiController {

    private final OffreEmploiService offreEmploiService;

    public OffreEmploiController(OffreEmploiService offreEmploiService) {
        this.offreEmploiService = offreEmploiService;
    }

    @GetMapping
    public List<OffreEmploi> findAll() {
        return offreEmploiService.listerToutesLesOffres();
    }

    @GetMapping("/{id}")
    public OffreEmploi findById(@PathVariable long id) {
        return offreEmploiService.consulterOffreParId(id);
    }

    @GetMapping("/type/{typeContrat}")
    public List<OffreEmploi> findByTypeContrat(@PathVariable TypeContrat typeContrat) {
        return offreEmploiService.listerOffresParTypeContrat(typeContrat);
    }

    @GetMapping("/localisation/{localisation}")
    public List<OffreEmploi> findByLocalisation(@PathVariable String localisation) {
        return offreEmploiService.listerOffresParLocalisation(localisation);
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
    public void delete(@PathVariable long id) {
        offreEmploiService.deleteOffre(id);
    }
}
