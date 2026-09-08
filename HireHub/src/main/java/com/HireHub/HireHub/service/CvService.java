package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CvService {

    private final CvRepository cvRepository;
    private final UserRepository userRepository;

    public CvService(CvRepository cvRepository, UserRepository userRepository) {
        this.cvRepository = cvRepository;
        this.userRepository = userRepository;
    }

    public Cv consulterCVparId(long cvId) {
        return cvRepository.findById(cvId).orElse(null);
    }

    public Cv consulterCVparNom(String cvNom) {
        return cvRepository.findByNomFichier(cvNom);
    }

    public List<Cv> listerAllCv() {
        return cvRepository.findAll();
    }

    public List<Cv> listerCVParCandidat(long candidatId) {
        return cvRepository.findByCandidatId(candidatId);
    }

    public Cv uploadCv(long candidatId, MultipartFile fichier) throws IOException {
        if (fichier.isEmpty() || !"application/pdf".equalsIgnoreCase(fichier.getContentType())) {
            throw new IllegalArgumentException("Le CV doit être un fichier PDF");
        }
        User candidat = userRepository.findById(candidatId).orElse(null);
        if (candidat == null) {
            throw new IllegalArgumentException("Candidat introuvable");
        }
        Cv cv = new Cv();
        cv.setCandidat(candidat);
        cv.setNomFichier(fichier.getOriginalFilename());
        cv.setCheminFichier("uploads/cv");
        cv.setContenu(fichier.getBytes());
        return cvRepository.save(cv);
    }

    public Cv remplacerCv(long cvId, MultipartFile fichier) throws IOException {
        Cv cv = consulterCVparId(cvId);
        if (cv == null) {
            throw new IllegalArgumentException("CV introuvable");
        }
        if (fichier.isEmpty() || !"application/pdf".equalsIgnoreCase(fichier.getContentType())) {
            throw new IllegalArgumentException("Le CV doit être un fichier PDF");
        }
        cv.setNomFichier(fichier.getOriginalFilename());
        cv.setCheminFichier("uploads/cv");
        cv.setContenu(fichier.getBytes());
        return cvRepository.save(cv);
    }

    public Cv updateCv(Cv cv) {
        return cvRepository.save(cv);
    }

    public String deleteCv(long cvId) {
        cvRepository.deleteById(cvId);
        return "CV supprimé avec succès";
    }
}