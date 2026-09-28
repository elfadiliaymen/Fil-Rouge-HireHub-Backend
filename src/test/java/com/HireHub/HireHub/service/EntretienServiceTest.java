package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.EntretienResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.entity.enums.StatutEntretien;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.EntretienRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntretienServiceTest {

    @Mock
    private EntretienRepository entretienRepository;

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private EntretienService entretienService;

    @Test
    void listerTousLesEntretiensLimiteLesEntretiensDuCandidatConnecte() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        User candidat = new User();
        candidat.setId(1L);
        candidat.setRole(Role.CANDIDAT);

        Entretien entretien = new Entretien();
        entretien.setId(1L);
        entretien.setDate(LocalDate.of(2026, 9, 15));
        entretien.setHeure(LocalTime.of(10, 0));
        entretien.setLieu("Salle B2");

        Candidature candidature = new Candidature();
        candidature.setId(1L);
        candidature.setCandidat(candidat);
        entretien.setCandidature(candidature);

        Page<Entretien> page = new PageImpl<>(List.of(entretien));

        when(currentUserService.isCandidat()).thenReturn(true);
        when(currentUserService.get()).thenReturn(candidat);
        when(entretienRepository.findByCandidatureCandidatId(1L, pageable)).thenReturn(page);

        // Act
        Page<EntretienResponse> result = entretienService.listerTousLesEntretiens(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Salle B2", result.getContent().get(0).getLieu());
        verify(entretienRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void enregistrerResultatMarqueLEntretienReussiPourSonRecruteur() {
        // Arrange
        User recruteur = new User();
        recruteur.setId(2L);
        recruteur.setRole(Role.RECRUTEUR);

        Candidature candidature = new Candidature();
        candidature.setId(5L);
        candidature.setStatut(StatutCandidature.EN_ATTENTE);

        Entretien entretien = new Entretien();
        entretien.setId(9L);
        entretien.setCandidature(candidature);
        entretien.setRecruteur(recruteur);
        entretien.setStatut(StatutEntretien.PLANIFIE);

        when(currentUserService.isCandidat()).thenReturn(false);
        when(currentUserService.hasRole(Role.RECRUTEUR)).thenReturn(true);
        when(currentUserService.get()).thenReturn(recruteur);
        when(entretienRepository.findById(9L)).thenReturn(Optional.of(entretien));
        when(entretienRepository.save(entretien)).thenReturn(entretien);
        when(candidatureRepository.save(candidature)).thenReturn(candidature);

        // Act
        EntretienResponse result = entretienService.enregistrerResultat(9L, StatutEntretien.REUSSI);

        // Assert
        assertEquals(StatutEntretien.REUSSI, result.getStatut());
        verify(entretienRepository).save(entretien);
        // candidature should be accepted when entretien is REUSSI
        assertEquals(StatutCandidature.ACCEPTEE, candidature.getStatut());
        verify(candidatureRepository).save(candidature);
    }
}
