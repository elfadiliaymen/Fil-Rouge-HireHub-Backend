package com.HireHub.HireHub.service;

import com.HireHub.HireHub.dto.LoginRequest;
import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.mapper.UserMapper;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> register(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("L'email existe déjà");
        }
        User user = UserMapper.toUser(request);
        return buildResponse(userRepository.save(user));
    }

    public Map<String, Object> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }
        if (!user.isActive()) {
            throw new IllegalArgumentException("Compte désactivé");
        }
        return buildResponse(user);
    }

    private Map<String, Object> buildResponse(User user) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> userPayload = new LinkedHashMap<>();
        userPayload.put("id", user.getId());
        userPayload.put("nom", user.getNom());
        userPayload.put("prenom", user.getPrenom());
        userPayload.put("email", user.getEmail());
        userPayload.put("role", user.getRole().name());
        userPayload.put("active", user.isActive());
        response.put("user", userPayload);
        return response;
    }
}