package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CandidatureRequest;
import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.dto.CandidatureStatsResponse;
import com.HireHub.HireHub.mapper.CandidatureMapper;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.entity.enums.StatutEntretien;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.EntretienRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final OffreEmploiRepository offreEmploiRepository;
    private final CvRepository cvRepository;
    private final EntretienRepository entretienRepository;
    private final CurrentUserService currentUserService;

    public CandidatureService(CandidatureRepository candidatureRepository,
                              UserRepository userRepository,
                              OffreEmploiRepository offreEmploiRepository,
                              CvRepository cvRepository,
                              EntretienRepository entretienRepository,
                              CurrentUserService currentUserService) {
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.offreEmploiRepository = offreEmploiRepository;
        this.cvRepository = cvRepository;
        this.entretienRepository = entretienRepository;
        this.currentUserService = currentUserService;
    }

    public CandidatureResponse consulterCandidatureParId(long candidatureId) {
        Candidature candidature = requerirCandidature(candidatureId);
        verifierAcces(candidature);
        return CandidatureMapper.toCandidatureResponse(candidature);
    }

    public Page<CandidatureResponse> listerToutesLesCandidatures(Pageable pageable) {
        interdireAuxCandidatEtRecruteur();
        return candidatureRepository.findAll(pageable).map(CandidatureMapper::toCandidatureResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParStatut(StatutCandidature statut, Pageable pageable) {
        if (currentUserService.isCandidat()) {
            return candidatureRepository
                    .findByStatutAndCandidatId(statut, currentUserService.get().getId(), pageable)
                    .map(CandidatureMapper::toCandidatureResponse);
        }
        if (currentUserService.isRecruteur()) {
            return candidatureRepository.findByStatutAndOffreRecruteurId(statut, currentUserService.get().getId(), pageable)
                    .map(CandidatureMapper::toCandidatureResponse);
        }
        return candidatureRepository.findByStatut(statut, pageable).map(CandidatureMapper::toCandidatureResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParCandidat(long candidatId, Pageable pageable) {
        if (currentUserService.isCandidat()) {
            if (candidatId != currentUserService.get().getId()) {
                throw introuvables("Candidatures introuvables pour le candidat " + candidatId);
            }
            return candidatureRepository.findByCandidatId(candidatId, pageable)
                    .map(CandidatureMapper::toCandidatureResponse);
        }
        if (currentUserService.isRecruteur()) {
            return candidatureRepository
                    .findByCandidatIdAndOffreRecruteurId(candidatId, currentUserService.get().getId(), pageable)
                    .map(CandidatureMapper::toCandidatureResponse);
        }
        return candidatureRepository.findByCandidatId(candidatId, pageable).map(CandidatureMapper::toCandidatureResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParRecruteur(long recruteurId, Pageable pageable) {
        if (currentUserService.isCandidat()) {
            throw introuvables("Candidatures introuvables pour le recruteur " + recruteurId);
        }
        if (currentUserService.isRecruteur()) {
            if (recruteurId != currentUserService.get().getId()) {
                throw introuvables("Candidatures introuvables pour le recruteur " + recruteurId);
            }
            return candidatureRepository.findByOffreRecruteurId(recruteurId, pageable)
                    .map(CandidatureMapper::toCandidatureResponse);
        }
        return candidatureRepository.findByOffreRecruteurId(recruteurId, pageable).map(CandidatureMapper::toCandidatureResponse);
    }

    public Page<CandidatureResponse> listerCandidaturesParOffre(long offreId, Pageable pageable) {
        OffreEmploi offre = offreEmploiRepository.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable avec l'id " + offreId));
        if (currentUserService.isRecruteur()
                && offre.getRecruteur().getId() != currentUserService.get().getId()) {
            throw notFoundCandidature(0);
        }
        return candidatureRepository.findByOffreId(offreId, pageable).map(CandidatureMapper::toCandidatureResponse);
    }

    public CandidatureResponse soumettreCandidature(CandidatureRequest request) {
        if (currentUserService.isRecruteur()) {
            throw new IllegalArgumentException("Un recruteur ne peut pas soumettre une candidature");
        }
        if (currentUserService.isCandidat()) {
            request.setCandidatId(currentUserService.get().getId());
        }

        if (candidatureRepository.existsByCandidatIdAndOffreId(request.getCandidatId(), request.getOffreId())) {
            throw new IllegalArgumentException("Vous avez déjà postulé à cette offre");
        }

        User candidat = userRepository.findById(request.getCandidatId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidat introuvable avec l'id " + request.getCandidatId()));
        if (candidat.getRole() != Role.CANDIDAT) {
            throw new IllegalArgumentException(
                    "La candidature doit être créée pour un utilisateur ayant le rôle CANDIDAT");
        }
        OffreEmploi offre = offreEmploiRepository.findById(request.getOffreId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable avec l'id " + request.getOffreId()));
        if (offre.getDateLimite() != null && offre.getDateLimite().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La date limite de cette offre est dépassée, les candidatures ne sont plus acceptées");
        }
        Cv cv = null;
        if (request.getCvId() != 0) {
            cv = cvRepository.findById(request.getCvId())
                    .orElseThrow(() -> new ResourceNotFoundException("CV introuvable avec l'id " + request.getCvId()));
            if (cv.getCandidat().getId() != request.getCandidatId()) {
                throw new IllegalArgumentException("Le CV sélectionné n'appartient pas au candidat");
            }
        }
        Candidature candidature = CandidatureMapper.toCandidature(request, candidat, offre, cv);
        try {
            Candidature saved = candidatureRepository.save(candidature);
            return CandidatureMapper.toCandidatureResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("Vous avez déjà postulé à cette offre");
        }
    }

    public CandidatureStatsResponse compterCandidaturesDuCandidatConnecte() {
        long candidatId = currentUserService.get().getId();
        return new CandidatureStatsResponse(
                candidatureRepository.countByCandidatId(candidatId),
                candidatureRepository.countByCandidatIdAndStatut(candidatId, StatutCandidature.EN_ATTENTE),
                candidatureRepository.countByCandidatIdAndStatut(candidatId, StatutCandidature.ACCEPTEE),
                candidatureRepository.countByCandidatIdAndStatut(candidatId, StatutCandidature.REFUSEE));
    }

    public CandidatureResponse changerStatut(long candidatureId, StatutCandidature statut) {
        Candidature candidature = requerirCandidature(candidatureId);
        verifierAcces(candidature);
        StatutCandidature actuel = candidature.getStatut();
        if (!transitionAutorisee(actuel, statut)) {
            throw new IllegalArgumentException(
                    "Transition de statut non autorisée : impossible de passer de " + actuel + " à " + statut);
        }
        verifierCoherenceAvecLesEntretiens(candidature, statut);
        candidature.setStatut(statut);
        Candidature saved = candidatureRepository.save(candidature);
        return CandidatureMapper.toCandidatureResponse(saved);
    }

    private boolean transitionAutorisee(StatutCandidature actuel, StatutCandidature cible) {
        if (actuel == cible) {
            return false;
        }
        return switch (actuel) {
            case EN_ATTENTE -> cible == StatutCandidature.ACCEPTEE || cible == StatutCandidature.REFUSEE;
            case ACCEPTEE -> cible == StatutCandidature.EN_ATTENTE;
            case REFUSEE -> false;
        };
    }

    private void verifierCoherenceAvecLesEntretiens(Candidature candidature, StatutCandidature cible) {
        if (cible == StatutCandidature.ACCEPTEE
                && !entretienRepository.existsByCandidatureIdAndStatut(candidature.getId(), StatutEntretien.REUSSI)) {
            throw new IllegalArgumentException(
                    "Impossible d'accepter une candidature sans entretien réussi");
        }
        if (cible == StatutCandidature.REFUSEE
                && entretienRepository.existsByCandidatureIdAndStatut(candidature.getId(), StatutEntretien.REUSSI)) {
            throw new IllegalArgumentException(
                    "Impossible de refuser une candidature dont l'entretien a été réussi");
        }
    }

    public String deleteCandidature(long candidatureId) {
        Candidature candidature = requerirCandidature(candidatureId);
        verifierAcces(candidature);
        entretienRepository.deleteByCandidatureId(candidatureId);
        candidatureRepository.deleteById(candidatureId);
        return "Candidature supprimée avec succès";
    }

    private Candidature requerirCandidature(long candidatureId) {
        return candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> notFoundCandidature(candidatureId));
    }

    private ResourceNotFoundException notFoundCandidature(long candidatureId) {
        return new ResourceNotFoundException("Candidature introuvable avec l'id " + candidatureId);
    }

    private ResourceNotFoundException introuvables(String message) {
        return new ResourceNotFoundException(message);
    }

    private void interdireAuxCandidatEtRecruteur() {
        if (currentUserService.isCandidat() || currentUserService.isRecruteur()) {
            throw new ResourceNotFoundException("Candidature introuvable avec l'id 0");
        }
    }

    private void verifierAcces(Candidature candidature) {
        if (currentUserService.isCandidat()) {
            if (candidature.getCandidat().getId() != currentUserService.get().getId()) {
                throw notFoundCandidature(candidature.getId());
            }
        } else if (currentUserService.isRecruteur()) {
            if (candidature.getOffre().getRecruteur().getId() != currentUserService.get().getId()) {
                throw notFoundCandidature(candidature.getId());
            }
        }
    }
}
