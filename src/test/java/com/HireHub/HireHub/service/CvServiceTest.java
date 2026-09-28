package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CvServiceTest {

    @Mock
    private CvRepository cvRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private CvService cvService;

    @Test
    void uploadCvRejetteFichierSansMagicPdfMemeSiContentTypeOk() throws Exception {
        // Arrange
        User candidat = new User();
        candidat.setId(1L);
        candidat.setNom("Dupont");
        candidat.setPrenom("Jean");
        candidat.setEmail("jean.dupont@example.com");
        candidat.setRole(Role.CANDIDAT);
        candidat.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(candidat));
        MockMultipartFile fichier = new MockMultipartFile(
                "file", "cv.pdf", "application/pdf",
                "ceci n'est pas un pdf".getBytes(StandardCharsets.UTF_8));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cvService.uploadCv(1L, fichier));
        verify(cvRepository, never()).save(any(Cv.class));
    }
}
