package com.HireHub.HireHub.service;

import com.HireHub.HireHub.config.JwtUtils;
import com.HireHub.HireHub.dto.LoginRequest;
import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.mapper.UserMapper;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public String register(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (request.getRole() != Role.CANDIDAT) {
            throw new IllegalArgumentException("L'inscription publique n'est autorisée qu'avec le rôle CANDIDAT");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("L'email existe déjà");
        }
        User user = userRepository.save(UserMapper.toUser(request));
        return jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole().name());
    }

    public String login(LoginRequest request) {
        String email = request.getEmail();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        } catch (AuthenticationException exception) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }
        return jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole().name());
    }
}
