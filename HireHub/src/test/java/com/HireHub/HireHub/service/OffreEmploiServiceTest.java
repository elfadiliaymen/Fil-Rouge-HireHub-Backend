package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.OffreRequest;
import com.HireHub.HireHub.dto.OffreResponse;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OffreEmploiServiceTest {

    @Mock
    private OffreEmploiRepository offreEmploiRepository;

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OffreEmploiService offreEmploiService;

    @Test
    void listerToutesLesOffres() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        User recruteur = new User();
        recruteur.setId(1L);
        recruteur.setNom("Dupont");
        recruteur.setPrenom("Jean");
        recruteur.setEmail("jean.dupont@example.com");
        recruteur.setRole(Role.RECRUTEUR);
        recruteur.setActive(true);

        OffreEmploi offre = new OffreEmploi();
        offre.setId(1L);
        offre.setTitre("Développeur Java");
        offre.setRecruteur(recruteur);

        Page<OffreEmploi> page =
                new PageImpl<>(List.of(offre));

        when(offreEmploiRepository.findAll(pageable))
                .thenReturn(page);

        // Act
        Page<OffreResponse> result =
                offreEmploiService.listerToutesLesOffres(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(
                "Développeur Java",
                result.getContent().get(0).getTitre()
        );
    }

    @Test
    void creerOffre() {
        // Arrange
        OffreRequest request = new OffreRequest();
        request.setTitre("Développeur Python");
        request.setRecruteurId(2L);
        request.setDescription("Développeur Python expérimenté");

        User recruteur = new User();
        recruteur.setId(2L);
        recruteur.setNom("Martin");
        recruteur.setPrenom("Sophie");
        recruteur.setEmail("sophie.martin@example.com");
        recruteur.setRole(Role.RECRUTEUR);
        recruteur.setActive(true);

        when(userRepository.findById(2L)).thenReturn(Optional.of(recruteur));

        OffreEmploi savedOffre = new OffreEmploi();
        savedOffre.setId(3L);
        savedOffre.setTitre("Développeur Python");
        savedOffre.setDescription("Développeur Python expérimenté");
        savedOffre.setRecruteur(recruteur);

        when(offreEmploiRepository.save(org.mockito.ArgumentMatchers.any(OffreEmploi.class)))
                .thenReturn(savedOffre);

        // Act
        OffreResponse result = offreEmploiService.creerOffre(request);

        // Assert
        assertNotNull(result);
        assertEquals("Développeur Python", result.getTitre());
        assertEquals(2L, result.getRecruteurId());
        assertEquals("Développeur Python expérimenté", result.getDescription());
    }
}