package com.HireHub.HireHub.service;

import com.HireHub.HireHub.mapper.EntretienMapper;
import com.HireHub.HireHub.dto.EntretienRequest;
import com.HireHub.HireHub.dto.EntretienResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.entity.enums.StatutEntretien;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.EntretienRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class EntretienService {

    private final EntretienRepository entretienRepository;
    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public EntretienService(EntretienRepository entretienRepository,
                            CandidatureRepository candidatureRepository,
                            UserRepository userRepository,
                            CurrentUserService currentUserService) {
        this.entretienRepository = entretienRepository;
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public EntretienResponse consulterEntretienParId(long entretienId) {
        Entretien entretien = requerirEntretien(entretienId);
        verifierAcces(entretien);
        return EntretienMapper.toEntretienResponse(entretien);
    }

    public Page<EntretienResponse> listerTousLesEntretiens(Pageable pageable) {
        if (currentUserService.isCandidat()) {
            return entretienRepository.findByCandidatureCandidatId(currentUserService.get().getId(), pageable)
                    .map(EntretienMapper::toEntretienResponse);
        }
        if (estRecruteurConnecte()) {
            return entretienRepository.findByRecruteurId(currentUserService.get().getId(), pageable)
                    .map(EntretienMapper::toEntretienResponse);
        }
        return entretienRepository.findAll(pageable).map(EntretienMapper::toEntretienResponse);
    }

    public Page<EntretienResponse> listerEntretiensParDate(LocalDate date, Pageable pageable) {
        if (currentUserService.isCandidat()) {
            return entretienRepository.findByDateAndCandidatureCandidatId(date, currentUserService.get().getId(),
                    pageable).map(EntretienMapper::toEntretienResponse);
        }
        if (estRecruteurConnecte()) {
            return entretienRepository.findByDateAndRecruteurId(date, currentUserService.get().getId(), pageable)
                    .map(EntretienMapper::toEntretienResponse);
        }
        return entretienRepository.findByDate(date, pageable).map(EntretienMapper::toEntretienResponse);
    }

    public Page<EntretienResponse> listerEntretiensParRecruteur(long recruteurId, Pageable pageable) {
        interdireAuxCandidats();
        if (estRecruteurConnecte()) {
            recruteurId = currentUserService.get().getId();
        }
        return entretienRepository.findByRecruteurId(recruteurId, pageable).map(EntretienMapper::toEntretienResponse);
    }

    public Page<EntretienResponse> listerEntretiensParCandidature(long candidatureId, Pageable pageable) {
        requerirCandidatureAccessible(candidatureId);
        if (currentUserService.isCandidat()) {
            return entretienRepository.findByCandidatureId(candidatureId, pageable)
                    .map(EntretienMapper::toEntretienResponse);
        }
        if (estRecruteurConnecte()) {
            return entretienRepository.findByCandidatureIdAndRecruteurId(candidatureId,
                    currentUserService.get().getId(), pageable).map(EntretienMapper::toEntretienResponse);
        }
        return entretienRepository.findByCandidatureId(candidatureId, pageable).map(EntretienMapper::toEntretienResponse);
    }

    public EntretienResponse planifierEntretien(EntretienRequest request) {
        interdireAuxCandidats();
        if (estRecruteurConnecte()) {
            request.setRecruteurId(currentUserService.get().getId());
        }
        Candidature candidature = requerirCandidatureAccessible(request.getCandidatureId());
        if (candidature.getStatut() == StatutCandidature.REFUSEE) {
            throw new IllegalArgumentException(
                    "Impossible de planifier un entretien pour une candidature refusée");
        }
        User recruteur = requerirRecruteur(request.getRecruteurId());
        verifierDatesEntretien(request.getDate(), candidature);
        Entretien entretien = EntretienMapper.toEntretien(request, candidature, recruteur);
        return EntretienMapper.toEntretienResponse(entretienRepository.save(entretien));
    }

    public EntretienResponse updateEntretien(long entretienId, EntretienRequest request) {
        interdireAuxCandidats();
        Entretien existant = requerirEntretien(entretienId);
        verifierProprietaireOuAdmin(existant);
        if (estRecruteurConnecte()) {
            request.setRecruteurId(currentUserService.get().getId());
        }
        Candidature candidature = requerirCandidatureAccessible(request.getCandidatureId());
        User recruteur = requerirRecruteur(request.getRecruteurId());
        verifierDatesEntretien(request.getDate(), candidature);
        existant.setDate(request.getDate());
        existant.setHeure(request.getHeure());
        existant.setLieu(request.getLieu());
        existant.setCandidature(candidature);
        existant.setRecruteur(recruteur);
        return EntretienMapper.toEntretienResponse(entretienRepository.save(existant));
    }

    public EntretienResponse enregistrerResultat(long entretienId, StatutEntretien statut) {
        interdireAuxCandidats();
        if (statut == null || statut == StatutEntretien.PLANIFIE) {
            throw new IllegalArgumentException("Le résultat de l'entretien doit être REUSSI, ECHEC ou ANNULE");
        }
        Entretien existant = requerirEntretien(entretienId);
        verifierProprietaireOuAdmin(existant);
        Candidature candidature = existant.getCandidature();
        if (candidature.getStatut() == StatutCandidature.REFUSEE) {
            throw new IllegalArgumentException(
                    "Impossible de modifier le résultat d'un entretien d'une candidature refusée");
        }
        if (candidature.getStatut() == StatutCandidature.ACCEPTEE && statut != StatutEntretien.REUSSI) {
            throw new IllegalArgumentException(
                    "Impossible de changer le résultat d'un entretien d'une candidature acceptée");
        }
        // Update entretien status
        existant.setStatut(statut);
        Entretien savedEntretien = entretienRepository.save(existant);

        // If entretien succeeded, accept the candidature automatically (if not already accepted)
        if (statut == StatutEntretien.REUSSI && candidature.getStatut() != StatutCandidature.ACCEPTEE) {
            candidature.setStatut(StatutCandidature.ACCEPTEE);
            candidatureRepository.save(candidature);
        }

        return EntretienMapper.toEntretienResponse(savedEntretien);
    }

    public String deleteEntretien(long entretienId) {
        interdireAuxCandidats();
        Entretien existant = requerirEntretien(entretienId);
        verifierProprietaireOuAdmin(existant);
        if (existant.getCandidature().getStatut() == StatutCandidature.ACCEPTEE) {
            throw new IllegalArgumentException("Impossible de supprimer l'entretien d'une candidature acceptée");
        }
        entretienRepository.deleteById(entretienId);
        return "Entretien supprimé avec succès";
    }

    private boolean estRecruteurConnecte() {
        return currentUserService.hasRole(Role.RECRUTEUR);
    }

    private void interdireAuxCandidats() {
        if (currentUserService.isCandidat()) {
            throw new ResourceNotFoundException("Entretien introuvable avec l'id 0");
        }
    }

    private Candidature requerirCandidatureAccessible(long candidatureId) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable avec l'id " + candidatureId));
        if (currentUserService.isCandidat()
                && candidature.getCandidat().getId() != currentUserService.get().getId()) {
            throw new ResourceNotFoundException("Candidature introuvable avec l'id " + candidatureId);
        }
        if (estRecruteurConnecte()
                && candidature.getOffre().getRecruteur().getId() != currentUserService.get().getId()) {
            throw new ResourceNotFoundException("Candidature introuvable avec l'id " + candidatureId);
        }
        return candidature;
    }

    private User requerirRecruteur(long recruteurId) {
        User recruteur = userRepository.findById(recruteurId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruteur introuvable avec l'id " + recruteurId));
        if (recruteur.getRole() != Role.RECRUTEUR) {
            throw new IllegalArgumentException("L'utilisateur sélectionné n'est pas un recruteur");
        }
        return recruteur;
    }

    private void verifierDatesEntretien(LocalDate date, Candidature candidature) {
        if (date == null || date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date de l'entretien doit être aujourd'hui ou dans le futur");
        }
        if (candidature.getDateCandidature() != null
                && date.isBefore(candidature.getDateCandidature().toLocalDate())) {
            throw new IllegalArgumentException("L'entretien ne peut pas avoir lieu avant la date de candidature");
        }
    }

    private void verifierProprietaireOuAdmin(Entretien entretien) {
        if (estRecruteurConnecte()
                && entretien.getRecruteur().getId() != currentUserService.get().getId()) {
            throw new ResourceNotFoundException("Entretien introuvable avec l'id " + entretien.getId());
        }
    }

    private void verifierAcces(Entretien entretien) {
        if (currentUserService.isCandidat() && !estCandidatDeLentretien(entretien)) {
            throw new ResourceNotFoundException("Entretien introuvable avec l'id " + entretien.getId());
        }
        verifierProprietaireOuAdmin(entretien);
    }

    private boolean estCandidatDeLentretien(Entretien entretien) {
        Candidature candidature = entretien.getCandidature();
        return candidature != null
                && candidature.getCandidat() != null
                && candidature.getCandidat().getId() == currentUserService.get().getId();
    }

    private Entretien requerirEntretien(long entretienId) {
        return entretienRepository.findById(entretienId)
                .orElseThrow(() -> new ResourceNotFoundException("Entretien introuvable avec l'id " + entretienId));
    }
}
