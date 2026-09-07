package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.repository.CvRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cv")
public class CvController {
    private final CvRepository cvRepository;

    public CvController(CvRepository cvRepository) {
        this.cvRepository = cvRepository;
    }

    @GetMapping
    public List<Cv> findAll(){
        return cvRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Cv> save(@RequestBody Cv cv){
        return ResponseEntity.status(HttpStatus.CREATED).body(cvRepository.save(cv));
    }

    @DeleteMapping("/{cvId}")
    public void delete(@PathVariable long cvId){
        cvRepository.deleteById(cvId);
    }

    @PutMapping
    public ResponseEntity<Cv> update(@RequestBody Cv cv){
        return ResponseEntity.ok(cvRepository.save(cv));
    }


}
