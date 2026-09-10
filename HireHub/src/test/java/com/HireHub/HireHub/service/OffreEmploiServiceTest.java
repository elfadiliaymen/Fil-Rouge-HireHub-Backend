package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.OffreResponse;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OffreEmploiServiceTest {

    @Mock
    private OffreEmploiRepository offreEmploiRepository;

    @InjectMocks
    private OffreEmploiService offreEmploiService;

    @Test
    void listerToutesLesOffres() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        OffreEmploi offre = new OffreEmploi();
        offre.setId(1L);
        offre.setTitre("Développeur Java");

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

}