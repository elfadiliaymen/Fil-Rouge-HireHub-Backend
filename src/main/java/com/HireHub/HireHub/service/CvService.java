package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CvFichier;
import com.HireHub.HireHub.dto.CvRequest;
import com.HireHub.HireHub.dto.CvResponse;
import com.HireHub.HireHub.mapper.CvMapper;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Service
@Transactional
public class CvService {

    private final CvRepository cvRepository;
    private final UserRepository userRepository;
    private final CandidatureRepository candidatureRepository;
    private final CurrentUserService currentUserService;

    public CvService(CvRepository cvRepository,
                     UserRepository userRepository,
                     CandidatureRepository candidatureRepository,
                     CurrentUserService currentUserService) {
        this.cvRepository = cvRepository;
        this.userRepository = userRepository;
        this.candidatureRepository = candidatureRepository;
        this.currentUserService = currentUserService;
    }

    public CvResponse consulterCVparId(long cvId) {
        verifierAcces(cvId);
        return CvMapper.toCvResponse(requerirCv(cvId));
    }

    public Page<CvResponse> listerAllCv(Pageable pageable) {
        interdireAuxCandidatEtRecruteur();
        return cvRepository.findAll(pageable).map(CvMapper::toCvResponse);
    }

    public Page<CvResponse> listerCVParRecruteur(long recruteurId, Pageable pageable) {
        if (currentUserService.isCandidat()) {
            throw new ResourceNotFoundException("CV introuvable avec l'id 0");
        }
        if (currentUserService.isRecruteur()) {
            if (recruteurId != currentUserService.get().getId()) {
                throw new ResourceNotFoundException("CV introuvable avec l'id 0");
            }
            return cvRepository.findByCandidaturesOffreRecruteurId(recruteurId, pageable).map(CvMapper::toCvResponse);
        }
        return cvRepository.findByCandidaturesOffreRecruteurId(recruteurId, pageable).map(CvMapper::toCvResponse);
    }

    public Page<CvResponse> listerCVParCandidat(long candidatId, Pageable pageable) {
        if (currentUserService.isCandidat()) {
            if (candidatId != currentUserService.get().getId()) {
                throw new ResourceNotFoundException("CV introuvable avec l'id 0");
            }
            return cvRepository.findByCandidatId(candidatId, pageable).map(CvMapper::toCvResponse);
        }
        if (currentUserService.isRecruteur()) {
            return cvRepository.findByCandidatIdAndCandidaturesOffreRecruteurId(
                            candidatId, currentUserService.get().getId(), pageable)
                    .map(CvMapper::toCvResponse);
        }
        return cvRepository.findByCandidatId(candidatId, pageable).map(CvMapper::toCvResponse);
    }

    public Page<CvResponse> listerCVParCandidatEtRecruteur(long candidatId, long recruteurId, Pageable pageable) {
        if (currentUserService.isCandidat()) {
            throw new ResourceNotFoundException("CV introuvable avec l'id 0");
        }
        if (currentUserService.isRecruteur()) {
            recruteurId = currentUserService.get().getId();
        }
        return cvRepository.findByCandidatIdAndCandidaturesOffreRecruteurId(candidatId, recruteurId, pageable)
                .map(CvMapper::toCvResponse);
    }

    public void verifierAcces(long cvId) {
        if (currentUserService.isCandidat()) {
            Cv cv = requerirCv(cvId);
            if (cv.getCandidat().getId() != currentUserService.get().getId()) {
                throw new ResourceNotFoundException("CV introuvable avec l'id " + cvId);
            }
        } else if (currentUserService.isRecruteur()) {
            if (!candidatureRepository.existsByCv_IdAndOffre_Recruteur_Id(cvId, currentUserService.get().getId())) {
                throw new ResourceNotFoundException("CV introuvable avec l'id " + cvId);
            }
        }
    }

    public CvFichier telechargerCv(long cvId) {
        verifierAcces(cvId);
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
        if (currentUserService.isRecruteur()) {
            throw new IllegalArgumentException("Un recruteur ne peut pas créer un CV");
        }
        if (currentUserService.isCandidat()) {
            candidatId = currentUserService.get().getId();
        }
        User candidat = requeteCandidat(candidatId);
        validerFichierPdf(fichier);
        Cv cv = new Cv();
        cv.setCandidat(candidat);
        cv.setNomFichier(nomFichierPropre(fichier));
        cv.setCheminFichier("uploads/cv");
        cv.setContenu(fichier.getBytes());
        return CvMapper.toCvResponse(cvRepository.save(cv));
    }

    public CvResponse remplacerCv(long cvId, MultipartFile fichier) throws IOException {
        verifyOwner(cvId);
        Cv cv = requerirCv(cvId);
        validerFichierPdf(fichier);
        cv.setNomFichier(nomFichierPropre(fichier));
        cv.setCheminFichier("uploads/cv");
        cv.setContenu(fichier.getBytes());
        return CvMapper.toCvResponse(cvRepository.save(cv));
    }

    public CvResponse creerCv(CvRequest request) {
        if (currentUserService.isRecruteur()) {
            throw new IllegalArgumentException("Un recruteur ne peut pas créer un CV");
        }
        if (currentUserService.isCandidat()) {
            request.setCandidatId(currentUserService.get().getId());
        }
        Cv cv = CvMapper.toCv(request, requeteCandidat(request.getCandidatId()));
        return CvMapper.toCvResponse(cvRepository.save(cv));
    }

    public CvResponse updateCv(long cvId, CvRequest request) {
        verifyOwner(cvId);
        Cv cv = requerirCv(cvId);
        if (currentUserService.isCandidat()) {
            request.setCandidatId(currentUserService.get().getId());
        }
        cv.setCandidat(requeteCandidat(request.getCandidatId()));
        cv.setNomFichier(request.getNomFichier());
        cv.setCheminFichier(request.getCheminFichier());
        return CvMapper.toCvResponse(cvRepository.save(cv));
    }

    public String deleteCv(long cvId) {
        verifyOwner(cvId);
        candidatureRepository.detacherCv(cvId);
        cvRepository.deleteById(cvId);
        return "CV supprimé avec succès";
    }

    private void validerFichierPdf(MultipartFile fichier) throws IOException {
        if (fichier == null || fichier.isEmpty()) {
            throw new IllegalArgumentException("Le CV doit être un fichier PDF");
        }
        if (fichier.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Le CV ne doit pas dépasser 5 Mo");
        }
        if (!"application/pdf".equalsIgnoreCase(fichier.getContentType())) {
            throw new IllegalArgumentException("Le CV doit être un fichier PDF");
        }
        try (InputStream flux = fichier.getInputStream()) {
            byte[] entete = flux.readNBytes(5);
            String magic = new String(entete, StandardCharsets.US_ASCII);
            if (!"%PDF-".equals(magic)) {
                throw new IllegalArgumentException("Le CV doit être un fichier PDF");
            }
        }
    }

    private String nomFichierPropre(MultipartFile fichier) {
        String nom = fichier.getOriginalFilename();
        if (nom == null || nom.isBlank()) {
            return "cv.pdf";
        }
        return Paths.get(nom).getFileName().toString();
    }

    private void verifyOwner(long cvId) {
        if (currentUserService.isCandidat()) {
            Cv cv = requerirCv(cvId);
            if (cv.getCandidat().getId() != currentUserService.get().getId()) {
                throw new ResourceNotFoundException("CV introuvable avec l'id " + cvId);
            }
        }
    }

    private void interdireAuxCandidatEtRecruteur() {
        if (currentUserService.isCandidat() || currentUserService.isRecruteur()) {
            throw new ResourceNotFoundException("CV introuvable avec l'id 0");
        }
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