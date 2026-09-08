package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
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

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByNom(String nom) {
        return userRepository.findByNom(nom);
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User creerUtilisateur(User user) {
        return userRepository.save(user);
    }

    public User inscrire(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.CANDIDAT);
        }
        user.setActive(true);
        return userRepository.save(user);
    }

    public User updateUtilisateur(long id, User user) {
        User existant = getUserById(id);
        if (existant == null) {
            return null;
        }
        if (user.getNom() != null) {
            existant.setNom(user.getNom());
        }
        if (user.getPrenom() != null) {
            existant.setPrenom(user.getPrenom());
        }
        if (user.getEmail() != null) {
            existant.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existant.setPassword(user.getPassword());
        }
        if (user.getRole() != null) {
            existant.setRole(user.getRole());
        }
        return userRepository.save(existant);
    }

    public User activer(long id) {
        User user = getUserById(id);
        if (user == null) {
            return null;
        }
        user.setActive(true);
        return userRepository.save(user);
    }

    public User desactiver(long id) {
        User user = getUserById(id);
        if (user == null) {
            return null;
        }
        user.setActive(false);
        return userRepository.save(user);
    }

    public String deleteUtilisateur(long id) {
        userRepository.deleteById(id);
        return "Utilisateur supprimé avec succès";
    }

    public Page<User> listerParRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
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
}