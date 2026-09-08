package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.DTOMapper;
import com.HireHub.HireHub.dto.EntretienRequest;
import com.HireHub.HireHub.dto.EntretienResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.EntretienRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EntretienService {

    private final EntretienRepository entretienRepository;
    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;

    public EntretienService(EntretienRepository entretienRepository,
                            CandidatureRepository candidatureRepository,
                            UserRepository userRepository) {
        this.entretienRepository = entretienRepository;
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
    }

    public EntretienResponse consulterEntretienParId(long entretienId) {
        return DTOMapper.toEntretienResponse(requerirEntretien(entretienId));
    }

    public Page<EntretienResponse> listerTousLesEntretiens(Pageable pageable) {
        return entretienRepository.findAll(pageable).map(DTOMapper::toEntretienResponse);
    }

    public Page<EntretienResponse> listerEntretiensParDate(LocalDate date, Pageable pageable) {
        return entretienRepository.findByDate(date, pageable).map(DTOMapper::toEntretienResponse);
    }

    public Page<EntretienResponse> listerEntretiensParRecruteur(long recruteurId, Pageable pageable) {
        return entretienRepository.findByRecruteurId(recruteurId, pageable).map(DTOMapper::toEntretienResponse);
    }

    public Page<EntretienResponse> listerEntretiensParCandidature(long candidatureId, Pageable pageable) {
        return entretienRepository.findByCandidatureId(candidatureId, pageable).map(DTOMapper::toEntretienResponse);
    }

    public EntretienResponse planifierEntretien(EntretienRequest request) {
        Candidature candidature = candidatureRepository.findById(request.getCandidatureId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable avec l'id " + request.getCandidatureId()));
        User recruteur = userRepository.findById(request.getRecruteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruteur introuvable avec l'id " + request.getRecruteurId()));
        Entretien entretien = DTOMapper.toEntretien(request, candidature, recruteur);
        return DTOMapper.toEntretienResponse(entretienRepository.save(entretien));
    }

    public EntretienResponse updateEntretien(long entretienId, EntretienRequest request) {
        Entretien existant = requerirEntretien(entretienId);
        existant.setDate(request.getDate());
        existant.setHeure(request.getHeure());
        existant.setLieu(request.getLieu());
        existant.setCandidature(candidatureRepository.findById(request.getCandidatureId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable avec l'id " + request.getCandidatureId())));
        existant.setRecruteur(userRepository.findById(request.getRecruteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruteur introuvable avec l'id " + request.getRecruteurId())));
        return DTOMapper.toEntretienResponse(entretienRepository.save(existant));
    }

    public String deleteEntretien(long entretienId) {
        entretienRepository.deleteById(entretienId);
        return "Entretien supprimé avec succès";
    }

    private Entretien requerirEntretien(long entretienId) {
        return entretienRepository.findById(entretienId)
                .orElseThrow(() -> new ResourceNotFoundException("Entretien introuvable avec l'id " + entretienId));
    }
}