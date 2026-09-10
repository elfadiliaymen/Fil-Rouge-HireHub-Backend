package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CvFichier;
import com.HireHub.HireHub.dto.CvRequest;
import com.HireHub.HireHub.dto.CvResponse;
import com.HireHub.HireHub.mapper.CvMapper;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CvService {

    private final CvRepository cvRepository;
    private final UserRepository userRepository;

    public CvService(CvRepository cvRepository, UserRepository userRepository) {
        this.cvRepository = cvRepository;
        this.userRepository = userRepository;
    }

    public CvResponse consulterCVparId(long cvId) {
        return CvMapper.toCvResponse(requerirCv(cvId));
    }

    public CvResponse consulterCVparNom(String cvNom) {
        Cv cv = cvRepository.findByNomFichier(cvNom);
        if (cv == null) {
            throw new ResourceNotFoundException("CV introuvable avec le nom " + cvNom);
        }
        return CvMapper.toCvResponse(cv);
    }

    public Page<CvResponse> listerAllCv(Pageable pageable) {
        return cvRepository.findAll(pageable).map(CvMapper::toCvResponse);
    }

    public Page<CvResponse> listerCVParCandidat(long candidatId, Pageable pageable) {
        return cvRepository.findByCandidatId(candidatId, pageable).map(CvMapper::toCvResponse);
    }

    public CvFichier telechargerCv(long cvId) {
        Cv cv = requerirCv(cvId);
        if (cv.getContenu() == null) {
            throw new ResourceNotFoundException("Le CV n'a pas de contenu enregistré");
        }
        CvFichier fichier = new CvFichier();
        fichier.setNomFichier(cv.getNomFichier());
        fichier.setContenu(cv.getContenu());
        return fichier;
    }

    public CvResponse uploadCv(long candidatId, MultipartFile fichier) throws IOException {
        User candidat = requeteCandidat(candidatId);
        if (fichier.isEmpty() || !"application/pdf".equalsIgnoreCase(fichier.getContentType())) {
            throw new IllegalArgumentException("Le CV doit être un fichier PDF");
        }
        Cv cv = new Cv();
        cv.setCandidat(candidat);
        cv.setNomFichier(fichier.getOriginalFilename());
        cv.setCheminFichier("uploads/cv");
        cv.setContenu(fichier.getBytes());
        return CvMapper.toCvResponse(cvRepository.save(cv));
    }

    public CvResponse remplacerCv(long cvId, MultipartFile fichier) throws IOException {
        Cv cv = requerirCv(cvId);
        if (fichier.isEmpty() || !"application/pdf".equalsIgnoreCase(fichier.getContentType())) {
            throw new IllegalArgumentException("Le CV doit être un fichier PDF");
        }
        cv.setNomFichier(fichier.getOriginalFilename());
        cv.setCheminFichier("uploads/cv");
        cv.setContenu(fichier.getBytes());
        return CvMapper.toCvResponse(cvRepository.save(cv));
    }

    public CvResponse creerCv(CvRequest request) {
        Cv cv = CvMapper.toCv(request, requeteCandidat(request.getCandidatId()));
        return CvMapper.toCvResponse(cvRepository.save(cv));
    }

    public CvResponse updateCv(long cvId, CvRequest request) {
        Cv cv = requerirCv(cvId);
        cv.setCandidat(requeteCandidat(request.getCandidatId()));
        cv.setNomFichier(request.getNomFichier());
        cv.setCheminFichier(request.getCheminFichier());
        return CvMapper.toCvResponse(cvRepository.save(cv));
    }

    public String deleteCv(long cvId) {
        cvRepository.deleteById(cvId);
        return "CV supprimé avec succès";
    }

    private Cv requerirCv(long cvId) {
        return cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("CV introuvable avec l'id " + cvId));
    }

    private User requeteCandidat(long candidatId) {
        return userRepository.findById(candidatId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidat introuvable avec l'id " + candidatId));
    }
}