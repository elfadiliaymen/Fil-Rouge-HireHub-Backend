package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CandidatureRequest;
import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.HireHub.HireHub.entity.enums.StatutEntretien;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.EntretienRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidatureServiceTest {

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OffreEmploiRepository offreEmploiRepository;

    @Mock
    private CvRepository cvRepository;

    @Mock
    private EntretienRepository entretienRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private CandidatureService candidatureService;

    @Test
    void soumettreCandidatureAvecCv() {
        // Arrange
        User candidat = new User();
        candidat.setId(1L);
        candidat.setNom("Dupont");
        candidat.setPrenom("Jean");
        candidat.setRole(Role.CANDIDAT);

        OffreEmploi offre = new OffreEmploi();
        offre.setId(1L);
        offre.setTitre("Développeur Java");
        offre.setRecruteur(candidat);

        Cv cv = new Cv();
        cv.setId(10L);
        cv.setCandidat(candidat);
        cv.setNomFichier("cv.pdf");

        CandidatureRequest request = new CandidatureRequest();
        request.setCandidatId(1L);
        request.setOffreId(1L);
        request.setCvId(10L);

        Candidature saved = new Candidature();
        saved.setId(1L);
        saved.setCandidat(candidat);
        saved.setOffre(offre);
        saved.setCv(cv);
        saved.setStatut(StatutCandidature.EN_ATTENTE);

        when(candidatureRepository.existsByCandidatIdAndOffreId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(candidat));
        when(offreEmploiRepository.findById(1L)).thenReturn(Optional.of(offre));
        when(cvRepository.findById(10L)).thenReturn(Optional.of(cv));
        when(candidatureRepository.save(any(Candidature.class))).thenReturn(saved);

        // Act
        CandidatureResponse result = candidatureService.soumettreCandidature(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getCv());
        assertEquals(10L, result.getCv().getId());
        assertEquals("cv.pdf", result.getCv().getNomFichier());
    }

    @Test
    void accepterUneCandidatureSansEntretienReussiEstRefuse() {
        // Arrange
        User recruteur = new User();
        recruteur.setId(2L);
        recruteur.setRole(Role.RECRUTEUR);

        OffreEmploi offre = new OffreEmploi();
        offre.setId(1L);
        offre.setRecruteur(recruteur);

        Candidature candidature = new Candidature();
        candidature.setId(7L);
        candidature.setStatut(StatutCandidature.EN_ATTENTE);
        candidature.setOffre(offre);

        when(candidatureRepository.findById(7L)).thenReturn(Optional.of(candidature));
        when(currentUserService.isCandidat()).thenReturn(false);
        when(currentUserService.isRecruteur()).thenReturn(true);
        when(currentUserService.get()).thenReturn(recruteur);
        when(entretienRepository.existsByCandidatureIdAndStatut(7L, StatutEntretien.REUSSI)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> candidatureService.changerStatut(7L, StatutCandidature.ACCEPTEE));
        assertEquals("Impossible d'accepter une candidature sans entretien réussi", exception.getMessage());
        verify(candidatureRepository, never()).save(any(Candidature.class));
    }
}
