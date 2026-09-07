package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.service.EntretienService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/entretiens")
public class EntretienController {

    private final EntretienService entretienService;

    public EntretienController(EntretienService entretienService) {
        this.entretienService = entretienService;
    }

    @GetMapping
    public List<Entretien> findAll() {
        return entretienService.listerTousLesEntretiens();
    }

    @GetMapping("/{id}")
    public Entretien findById(@PathVariable long id) {
        return entretienService.consulterEntretienParId(id);
    }

    @GetMapping("/date/{date}")
    public List<Entretien> findByDate(@PathVariable LocalDate date) {
        return entretienService.listerEntretiensParDate(date);
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
    public void delete(@PathVariable long id) {
        entretienService.deleteEntretien(id);
    }
}
