package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.dto.CvFichier;
import com.HireHub.HireHub.dto.CvRequest;
import com.HireHub.HireHub.dto.CvResponse;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.service.CvService;
import com.HireHub.HireHub.service.CurrentUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/cv")
public class CvController {

    private final CvService cvService;
    private final CurrentUserService currentUserService;

    public CvController(CvService cvService, CurrentUserService currentUserService) {
        this.cvService = cvService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public Page<CvResponse> findAll(@PageableDefault(sort = "id") Pageable pageable) {
        if (currentUserService.isCandidat()) {
            return cvService.listerCVParCandidat(currentUserService.get().getId(), pageable);
        }
        return cvService.listerAllCv(pageable);
    }

    @GetMapping("/{cvId}")
    public CvResponse findById(@PathVariable long cvId) {
        CvResponse cv = cvService.consulterCVparId(cvId);

        if (currentUserService.isCandidat() && cv.getCandidat().getId() != currentUserService.get().getId()) {
            throw new ResourceNotFoundException("CV introuvable avec l'id " + cvId);
        }

        return cv;
    }

    @GetMapping("/candidat/{candidatId}")
    public Page<CvResponse> findByCandidat(@PathVariable long candidatId,
                                           @PageableDefault(sort = "id") Pageable pageable) {
        if (currentUserService.isCandidat()) {
            candidatId = currentUserService.get().getId();
        }
        return cvService.listerCVParCandidat(candidatId, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    @PostMapping("/upload")
    public CvResponse upload(@RequestParam long candidatId, @RequestParam("file") MultipartFile fichier) throws IOException {
        return cvService.uploadCv(candidatId, fichier);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    @PutMapping("/upload/{cvId}")
    public CvResponse remplacer(@PathVariable long cvId, @RequestParam("file") MultipartFile fichier) throws IOException {
        return cvService.remplacerCv(cvId, fichier);
    }

    @GetMapping("/download/{cvId}")
    public ResponseEntity<byte[]> download(@PathVariable long cvId) {
        if (currentUserService.isCandidat()) {
            CvResponse cv = cvService.consulterCVparId(cvId);
            if (cv.getCandidat().getId() != currentUserService.get().getId()) {
                throw new ResourceNotFoundException("CV introuvable avec l'id " + cvId);
            }
        }
        CvFichier cvFichier = cvService.telechargerCv(cvId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cvFichier.getNomFichier() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(cvFichier.getContenu());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    @PostMapping
    public ResponseEntity<CvResponse> save(@RequestBody CvRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cvService.creerCv(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    @PutMapping("/{cvId}")
    public CvResponse update(@PathVariable long cvId, @RequestBody CvRequest request) {
        return cvService.updateCv(cvId, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    @DeleteMapping("/{cvId}")
    public String delete(@PathVariable long cvId) {
        return cvService.deleteCv(cvId);
    }
}