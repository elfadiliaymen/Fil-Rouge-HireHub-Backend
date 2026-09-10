package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.UserRequest;
import com.HireHub.HireHub.dto.UserResponse;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setNom("Dupont");
        user.setPrenom("Jean");
        user.setEmail("jean.dupont@example.com");
        user.setRole(Role.CANDIDAT);
        user.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserResponse result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Dupont", result.getNom());
        assertEquals("Jean", result.getPrenom());
        assertEquals("jean.dupont@example.com", result.getEmail());
    }

    @Test
    void getUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(99L));
    }

    @Test
    void creerUtilisateur() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setNom("Martin");
        request.setPrenom("Sophie");
        request.setEmail("sophie.martin@example.com");
        request.setPassword("secret123");
        request.setRole(Role.CANDIDAT);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setNom("Martin");
        savedUser.setPrenom("Sophie");
        savedUser.setEmail("sophie.martin@example.com");
        savedUser.setRole(Role.CANDIDAT);
        savedUser.setActive(true);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponse result = userService.creerUtilisateur(request);

        // Assert
        assertNotNull(result);
        assertEquals("Martin", result.getNom());
        assertEquals("Sophie", result.getPrenom());
        assertEquals("sophie.martin@example.com", result.getEmail());
    }

    @Test
    void deleteUtilisateur() {
        // Arrange & Act
        String result = userService.deleteUtilisateur(1L);

        // Assert
        assertEquals("Utilisateur supprimé avec succès", result);
    }
}
