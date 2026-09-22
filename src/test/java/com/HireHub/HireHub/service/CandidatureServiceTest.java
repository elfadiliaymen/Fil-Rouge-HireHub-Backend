package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidatureServiceTest {

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OffreEmploiRepository offreEmploiRepository;

    @InjectMocks
    private CandidatureService candidatureService;

    @Test
    void consulterCandidatureParId() {
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
        candidature.setStatut(StatutCandidature.EN_ATTENTE);

        when(candidatureRepository.findById(1L)).thenReturn(Optional.of(candidature));

        // Act
        CandidatureResponse result = candidatureService.consulterCandidatureParId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(StatutCandidature.EN_ATTENTE, result.getStatut());
    }

    @Test
    void listerToutesLesCandidatures() {
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
        candidature.setStatut(StatutCandidature.EN_ATTENTE);

        Page<Candidature> page = new PageImpl<>(List.of(candidature));

        when(candidatureRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<CandidatureResponse> result =
                candidatureService.listerToutesLesCandidatures(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(StatutCandidature.EN_ATTENTE,
                result.getContent().get(0).getStatut());
    }

    @Test
    void deleteCandidature() {
        // Arrange & Act
        String result = candidatureService.deleteCandidature(1L);

        // Assert
        assertEquals("Candidature supprimée avec succès", result);
    }
}
