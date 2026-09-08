package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.service.EntretienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/entretiens")
public class EntretienController {

    private final EntretienService entretienService;

    public EntretienController(EntretienService entretienService) {
        this.entretienService = entretienService;
    }

    @GetMapping
    public Page<Entretien> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return entretienService.listerTousLesEntretiens(pageable);
    }

    @GetMapping("/{id}")
    public Entretien findById(@PathVariable long id) {
        return entretienService.consulterEntretienParId(id);
    }

    @GetMapping("/date/{date}")
    public Page<Entretien> findByDate(@PathVariable LocalDate date,
                                      @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return entretienService.listerEntretiensParDate(date, pageable);
    }

    @GetMapping("/recruteur/{recruteurId}")
    public Page<Entretien> findByRecruteur(@PathVariable long recruteurId,
                                           @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return entretienService.listerEntretiensParRecruteur(recruteurId, pageable);
    }

    @GetMapping("/candidature/{candidatureId}")
    public Page<Entretien> findByCandidature(@PathVariable long candidatureId,
                                             @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return entretienService.listerEntretiensParCandidature(candidatureId, pageable);
    }

    @PostMapping
    public ResponseEntity<Entretien> save(@RequestBody Entretien entretien) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entretienService.planifierEntretien(entretien));
    }

    @PutMapping
    public ResponseEntity<Entretien> update(@RequestBody Entretien entretien) {
        return ResponseEntity.ok(entretienService.updateEntretien(entretien));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return entretienService.deleteEntretien(id);
    }
}