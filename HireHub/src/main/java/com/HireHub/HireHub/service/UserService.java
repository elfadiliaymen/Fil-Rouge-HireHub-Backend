package com.HireHub.HireHub.service;

import com.HireHub.HireHub.mapper.UserMapper;
import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.dto.UserRequest;
import com.HireHub.HireHub.dto.UserResponse;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toUserResponse);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec l'email " + email);
        }
        return UserMapper.toUserResponse(user);
    }

    public UserResponse getUserByNom(String nom) {
        User user = userRepository.findByNom(nom);
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec le nom " + nom);
        }
        return UserMapper.toUserResponse(user);
    }

    public UserResponse getUserById(long id) {
        return UserMapper.toUserResponse(requerirUtilisateur(id));
    }

    public UserResponse creerUtilisateur(UserRequest request) {
        User user = UserMapper.toUser(request);
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse inscrire(RegisterRequest request) {
        User user = UserMapper.toUser(request);
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUtilisateur(long id, UserRequest request) {
        User existant = requerirUtilisateur(id);
        if (request.getNom() != null) {
            existant.setNom(request.getNom());
        }
        if (request.getPrenom() != null) {
            existant.setPrenom(request.getPrenom());
        }
        if (request.getEmail() != null) {
            existant.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            existant.setPassword(request.getPassword());
        }
        if (request.getRole() != null) {
            existant.setRole(request.getRole());
        }
        return UserMapper.toUserResponse(userRepository.save(existant));
    }

    public UserResponse activer(long id) {
        User user = requerirUtilisateur(id);
        user.setActive(true);
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse desactiver(long id) {
        User user = requerirUtilisateur(id);
        user.setActive(false);
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    public String deleteUtilisateur(long id) {
        userRepository.deleteById(id);
        return "Utilisateur supprimé avec succès";
    }

    public Page<UserResponse> listerParRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable).map(UserMapper::toUserResponse);
    }

    public Map<String, Long> statistiques() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("total", userRepository.count());
        stats.put("actifs", userRepository.findAll().stream().filter(User::isActive).count());
        stats.put("inactifs", userRepository.findAll().stream().filter(user -> !user.isActive()).count());
        stats.put("administrateurs", userRepository.countByRole(Role.ADMIN));
        stats.put("recruteurs", userRepository.countByRole(Role.RECRUTEUR));
        stats.put("candidats", userRepository.countByRole(Role.CANDIDAT));
        return stats;
    }

    private User requerirUtilisateur(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id " + id));
    }
}