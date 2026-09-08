package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.service.CvService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/cv")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping
    public Page<Cv> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return cvService.listerAllCv(pageable);
    }

    @GetMapping("/{cvId}")
    public Cv findById(@PathVariable long cvId) {
        return cvService.consulterCVparId(cvId);
    }

    @GetMapping("/candidat/{candidatId}")
    public Page<Cv> findByCandidat(@PathVariable long candidatId,
                                   @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return cvService.listerCVParCandidat(candidatId, pageable);
    }

    @PostMapping("/upload")
    public ResponseEntity<Cv> upload(@RequestParam long candidatId, @RequestParam("file") MultipartFile fichier) {
        try {
            Cv cv = cvService.uploadCv(candidatId, fichier);
            return ResponseEntity.status(HttpStatus.CREATED).body(cv);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/upload/{cvId}")
    public ResponseEntity<Cv> remplacer(@PathVariable long cvId, @RequestParam("file") MultipartFile fichier) {
        try {
            return ResponseEntity.ok(cvService.remplacerCv(cvId, fichier));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{cvId}")
    public ResponseEntity<byte[]> download(@PathVariable long cvId) {
        Cv cv = cvService.consulterCVparId(cvId);
        if (cv == null || cv.getContenu() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cv.getNomFichier() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(cv.getContenu());
    }

    @PostMapping
    public ResponseEntity<Cv> save(@RequestBody Cv cv) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cvService.updateCv(cv));
    }

    @PutMapping
    public ResponseEntity<Cv> update(@RequestBody Cv cv) {
        return ResponseEntity.ok(cvService.updateCv(cv));
    }

    @DeleteMapping("/{cvId}")
    public String delete(@PathVariable long cvId) {
        return cvService.deleteCv(cvId);
    }
}