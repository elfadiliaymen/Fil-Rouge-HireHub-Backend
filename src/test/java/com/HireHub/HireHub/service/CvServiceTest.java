package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.CvRequest;
import com.HireHub.HireHub.dto.CvResponse;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CvRepository;
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
class CvServiceTest {

    @Mock
    private CvRepository cvRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CvService cvService;

    @Test
    void consulterCVparId() {
        // Arrange
        User candidat = new User();
        candidat.setId(1L);
        candidat.setNom("Dupont");
        candidat.setPrenom("Jean");
        candidat.setEmail("jean.dupont@example.com");
        candidat.setRole(Role.CANDIDAT);
        candidat.setActive(true);

        Cv cv = new Cv();
        cv.setId(1L);
        cv.setNomFichier("cv_jean.pdf");
        cv.setCheminFichier("uploads/cv");
        cv.setCandidat(candidat);

        when(cvRepository.findById(1L)).thenReturn(Optional.of(cv));

        // Act
        CvResponse result = cvService.consulterCVparId(1L);

        // Assert
        assertNotNull(result);
        assertEquals("cv_jean.pdf", result.getNomFichier());
    }

    @Test
    void consulterCVparNomNotFound() {
        // Arrange
        when(cvRepository.findByNomFichier("inexistant.pdf")).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> cvService.consulterCVparNom("inexistant.pdf"));
    }

    @Test
    void creerCv() {
        // Arrange
        User candidat = new User();
        candidat.setId(1L);
        candidat.setNom("Dupont");
        candidat.setPrenom("Jean");
        candidat.setEmail("jean.dupont@example.com");
        candidat.setRole(Role.CANDIDAT);
        candidat.setActive(true);

        CvRequest request = new CvRequest();
        request.setCandidatId(1L);
        request.setNomFichier("cv_jean.pdf");
        request.setCheminFichier("uploads/cv");

        when(userRepository.findById(1L)).thenReturn(Optional.of(candidat));

        Cv savedCv = new Cv();
        savedCv.setId(1L);
        savedCv.setNomFichier("cv_jean.pdf");
        savedCv.setCheminFichier("uploads/cv");
        savedCv.setCandidat(candidat);

        when(cvRepository.save(any(Cv.class))).thenReturn(savedCv);

        // Act
        CvResponse result = cvService.creerCv(request);

        // Assert
        assertNotNull(result);
        assertEquals("cv_jean.pdf", result.getNomFichier());
        assertEquals("uploads/cv", result.getCheminFichier());
    }
}
