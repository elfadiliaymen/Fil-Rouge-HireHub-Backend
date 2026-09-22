package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.EntretienResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntretienServiceTest {

    @Mock
    private EntretienRepository entretienRepository;

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EntretienService entretienService;

    @Test
    void consulterEntretienParId() {
        // Arrange
        User candidat = new User();
        candidat.setId(1L);
        candidat.setNom("Dupont");
        candidat.setPrenom("Jean");
        candidat.setEmail("jean.dupont@example.com");
        candidat.setRole(Role.CANDIDAT);
        candidat.setActive(true);

        OffreEmploi offre = new OffreEmploi();
        offre.setId(1L);
        offre.setTitre("Développeur Java");
        offre.setRecruteur(candidat);

        Candidature candidature = new Candidature();
        candidature.setId(1L);
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        candidature.setStatut(StatutCandidature.ACCEPTEE);

        User recruteur = new User();
        recruteur.setId(2L);
        recruteur.setNom("Martin");
        recruteur.setPrenom("Sophie");
        recruteur.setEmail("sophie.martin@example.com");
        recruteur.setRole(Role.RECRUTEUR);
        recruteur.setActive(true);

        Entretien entretien = new Entretien();
        entretien.setId(1L);
        entretien.setDate(LocalDate.of(2026, 9, 15));
        entretien.setHeure(LocalTime.of(10, 0));
        entretien.setLieu("Salle B2");
        entretien.setCandidature(candidature);
        entretien.setRecruteur(recruteur);

        when(entretienRepository.findById(1L)).thenReturn(Optional.of(entretien));

        // Act
        EntretienResponse result = entretienService.consulterEntretienParId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.of(2026, 9, 15), result.getDate());
        assertEquals("Salle B2", result.getLieu());
        assertEquals(1L, result.getCandidatureId());
    }

    @Test
    void listerTousLesEntretiens() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        User candidat = new User();
        candidat.setId(1L);
        candidat.setNom("Dupont");
        candidat.setPrenom("Jean");
        candidat.setEmail("jean.dupont@example.com");
        candidat.setRole(Role.CANDIDAT);
        candidat.setActive(true);

        OffreEmploi offre = new OffreEmploi();
        offre.setId(1L);
        offre.setTitre("Développeur Java");
        offre.setRecruteur(candidat);

        Candidature candidature = new Candidature();
        candidature.setId(1L);
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        candidature.setStatut(StatutCandidature.ACCEPTEE);

        User recruteur = new User();
        recruteur.setId(2L);
        recruteur.setNom("Martin");
        recruteur.setPrenom("Sophie");
        recruteur.setEmail("sophie.martin@example.com");
        recruteur.setRole(Role.RECRUTEUR);
        recruteur.setActive(true);

        Entretien entretien = new Entretien();
        entretien.setId(1L);
        entretien.setDate(LocalDate.of(2026, 9, 15));
        entretien.setHeure(LocalTime.of(10, 0));
        entretien.setLieu("Salle B2");
        entretien.setCandidature(candidature);
        entretien.setRecruteur(recruteur);

        Page<Entretien> page = new PageImpl<>(List.of(entretien));

        when(entretienRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<EntretienResponse> result =
                entretienService.listerTousLesEntretiens(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Salle B2", result.getContent().get(0).getLieu());
    }

    @Test
    void deleteEntretien() {
        // Arrange & Act
        String result = entretienService.deleteEntretien(1L);

        // Assert
        assertEquals("Entretien supprimé avec succès", result);
    }
}
