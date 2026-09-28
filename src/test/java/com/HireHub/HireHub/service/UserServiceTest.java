package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.UserRequest;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntretienRepository entretienRepository;

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private OffreEmploiRepository offreEmploiRepository;

    @Mock
    private CvRepository cvRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void updateUtilisateurAccepteUneMiseAJourPartielle() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setNom("Jean");
        user.setPrenom("Dupont");
        user.setEmail("jean.dupont@example.com");
        user.setPassword("initialPasswordValue");
        user.setRole(Role.CANDIDAT);

        UserRequest request = new UserRequest();
        request.setTelephone("0611223344");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.updateUtilisateur(1L, request);

        // Assert
        assertEquals("0611223344", user.getTelephone());
        assertEquals("Jean", user.getNom());
        assertEquals("initialPasswordValue", user.getPassword());
    }
}
