package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.dto.CvFichier;
import com.HireHub.HireHub.dto.CvRequest;
import com.HireHub.HireHub.dto.CvResponse;
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
    public Page<CvResponse> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return cvService.listerAllCv(pageable);
    }

    @GetMapping("/{cvId}")
    public CvResponse findById(@PathVariable long cvId) {
        return cvService.consulterCVparId(cvId);
    }

    @GetMapping("/candidat/{candidatId}")
    public Page<CvResponse> findByCandidat(@PathVariable long candidatId,
                                           @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return cvService.listerCVParCandidat(candidatId, pageable);
    }

    @PostMapping("/upload")
    public CvResponse upload(@RequestParam long candidatId, @RequestParam("file") MultipartFile fichier) throws IOException {
        return cvService.uploadCv(candidatId, fichier);
    }

    @PutMapping("/upload/{cvId}")
    public CvResponse remplacer(@PathVariable long cvId, @RequestParam("file") MultipartFile fichier) throws IOException {
        return cvService.remplacerCv(cvId, fichier);
    }

    @GetMapping("/download/{cvId}")
    public ResponseEntity<byte[]> download(@PathVariable long cvId) {
        CvFichier cvFichier = cvService.telechargerCv(cvId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cvFichier.getNomFichier() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(cvFichier.getContenu());
    }

    @PostMapping
    public ResponseEntity<CvResponse> save(@RequestBody CvRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cvService.creerCv(request));
    }

    @PutMapping("/{cvId}")
    public CvResponse update(@PathVariable long cvId, @RequestBody CvRequest request) {
        return cvService.updateCv(cvId, request);
    }

    @DeleteMapping("/{cvId}")
    public String delete(@PathVariable long cvId) {
        return cvService.deleteCv(cvId);
    }
}