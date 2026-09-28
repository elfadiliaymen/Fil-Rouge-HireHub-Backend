package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.OffreResponse;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.EntretienRepository;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OffreEmploiServiceTest {

    @Mock
    private OffreEmploiRepository offreEmploiRepository;

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntretienRepository entretienRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private OffreEmploiService offreEmploiService;

    @Test
    void rechercherOffresAvecMotCleRechercheCoteServeur() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        User recruteur = new User();
        recruteur.setId(1L);
        recruteur.setRole(Role.RECRUTEUR);

        OffreEmploi offre = new OffreEmploi();
        offre.setId(2L);
        offre.setTitre("Data Analyst");
        offre.setLocalisation("Paris");
        offre.setRecruteur(recruteur);

        Page<OffreEmploi> page = new PageImpl<>(List.of(offre));

        when(offreEmploiRepository.rechercherOffresActives(any(LocalDate.class), eq("data"), isNull(), eq(pageable)))
                .thenReturn(page);

        // Act
        Page<OffreResponse> result = offreEmploiService.rechercherOffres("data", null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Data Analyst", result.getContent().get(0).getTitre());
    }
}
