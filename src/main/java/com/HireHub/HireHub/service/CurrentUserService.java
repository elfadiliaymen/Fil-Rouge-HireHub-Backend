package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec l'email " + email);
        }

        return user;
    }

    public boolean hasRole(Role role) {
        User user = getUserOrNull();
        return user != null && user.getRole() == role;
    }

    public boolean isCandidat() {
        return hasRole(Role.CANDIDAT);
    }

    public boolean isRecruteur() {
        return hasRole(Role.RECRUTEUR);
    }

    private User getUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || authentication.getName() == null
                || authentication.getName().equals("anonymousUser")) {
            return null;
        }

        return userRepository.findByEmail(authentication.getName());
    }
}