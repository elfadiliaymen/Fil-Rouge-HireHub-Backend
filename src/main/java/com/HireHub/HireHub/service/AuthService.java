package com.HireHub.HireHub.service;

import com.HireHub.HireHub.config.JwtUtils;
import com.HireHub.HireHub.dto.LoginRequest;
import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.mapper.UserMapper;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public String register(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("L'inscription avec le rôle ADMIN n'est pas autorisée");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("L'email existe déjà");
        }
        User user = userRepository.save(UserMapper.toUser(request));
        return jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole().name());
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }
        if (!user.isActive()) {
            throw new IllegalArgumentException("Compte désactivé");
        }
        return jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole().name());
    }
}