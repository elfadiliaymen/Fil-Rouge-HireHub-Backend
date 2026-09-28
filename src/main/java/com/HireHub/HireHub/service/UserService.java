package com.HireHub.HireHub.service;

import com.HireHub.HireHub.mapper.UserMapper;
import com.HireHub.HireHub.dto.ChangePasswordRequest;
import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.dto.UpdateProfileRequest;
import com.HireHub.HireHub.dto.UserRequest;
import com.HireHub.HireHub.dto.UserResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.exception.ResourceNotFoundException;
import com.HireHub.HireHub.repository.CandidatureRepository;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.EntretienRepository;
import com.HireHub.HireHub.repository.OffreEmploiRepository;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntretienRepository entretienRepository;
    private final CandidatureRepository candidatureRepository;
    private final OffreEmploiRepository offreEmploiRepository;
    private final CvRepository cvRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EntretienRepository entretienRepository,
                       CandidatureRepository candidatureRepository,
                       OffreEmploiRepository offreEmploiRepository,
                       CvRepository cvRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entretienRepository = entretienRepository;
        this.candidatureRepository = candidatureRepository;
        this.offreEmploiRepository = offreEmploiRepository;
        this.cvRepository = cvRepository;
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

    public Page<UserResponse> listerParNom(String nom, Pageable pageable) {
        return userRepository.findByNom(nom, pageable).map(UserMapper::toUserResponse);
    }

    public UserResponse getUserById(long id) {
        return UserMapper.toUserResponse(requerirUtilisateur(id));
    }

    public UserResponse creerUtilisateur(UserRequest request) {
        verifierChampsObligatoires(request);
        verifierEmailUnique(request.getEmail());
        User user = UserMapper.toUser(request);
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse inscrire(RegisterRequest request) {
        return creerUtilisateur(UserMapper.toUserRequest(request));
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
            existant.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null) {
            existant.setRole(request.getRole());
        }
        if (request.getTelephone() != null) {
            existant.setTelephone(request.getTelephone());
        }
        if (request.getAdresse() != null) {
            existant.setAdresse(request.getAdresse());
        }
        if (request.getEntreprise() != null) {
            existant.setEntreprise(request.getEntreprise());
        }
        if (request.getPoste() != null) {
            existant.setPoste(request.getPoste());
        }
        if (request.getTelephonePro() != null) {
            existant.setTelephonePro(request.getTelephonePro());
        }
        if (request.getDateNaissance() != null) {
            existant.setDateNaissance(request.getDateNaissance());
        }
        if (request.getNiveauEtude() != null) {
            existant.setNiveauEtude(request.getNiveauEtude());
        }
        if (request.getExperienceAnnees() != null) {
            validerExperienceAnnees(request.getExperienceAnnees());
            existant.setExperienceAnnees(request.getExperienceAnnees());
        }
        if (request.getLinkedinUrl() != null) {
            existant.setLinkedinUrl(request.getLinkedinUrl());
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
        entretienRepository.deleteByRecruteurId(id);

        List<Candidature> candidaturesCandidat = candidatureRepository.findByCandidatId(id);
        for (Candidature candidature : candidaturesCandidat) {
            entretienRepository.deleteByCandidatureId(candidature.getId());
        }
        candidatureRepository.deleteAll(candidaturesCandidat);

        List<OffreEmploi> offres = offreEmploiRepository.findByRecruteurId(id);
        for (OffreEmploi offre : offres) {
            List<Candidature> candidaturesOffre = candidatureRepository.findByOffreId(offre.getId());
            for (Candidature candidature : candidaturesOffre) {
                entretienRepository.deleteByCandidatureId(candidature.getId());
                candidatureRepository.delete(candidature);
            }
        }
        offreEmploiRepository.deleteAll(offres);

        cvRepository.deleteByCandidatId(id);

        userRepository.deleteById(id);
        return "Utilisateur supprimé avec succès";
    }

    public Page<UserResponse> listerParRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable).map(UserMapper::toUserResponse);
    }

    public Map<String, Long> statistiques() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("total", userRepository.count());
        stats.put("actifs", userRepository.countByActiveTrue());
        stats.put("inactifs", userRepository.countByActiveFalse());
        stats.put("administrateurs", userRepository.countByRole(Role.ADMIN));
        stats.put("recruteurs", userRepository.countByRole(Role.RECRUTEUR));
        stats.put("candidats", userRepository.countByRole(Role.CANDIDAT));
        return stats;
    }

    public UserResponse getMe(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec l'email " + email);
        }
        return UserMapper.toUserResponse(user);
    }

    public UserResponse updateMe(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec l'email " + email);
        }
        if (request.getNom() != null && !request.getNom().isBlank()) {
            user.setNom(request.getNom());
        }
        if (request.getPrenom() != null && !request.getPrenom().isBlank()) {
            user.setPrenom(request.getPrenom());
        }
        if (request.getTelephone() != null) {
            user.setTelephone(request.getTelephone());
        }
        if (request.getAdresse() != null) {
            user.setAdresse(request.getAdresse());
        }
        if (request.getEntreprise() != null) {
            user.setEntreprise(request.getEntreprise());
        }
        if (request.getPoste() != null) {
            user.setPoste(request.getPoste());
        }
        if (request.getTelephonePro() != null) {
            user.setTelephonePro(request.getTelephonePro());
        }
        if (request.getDateNaissance() != null) {
            user.setDateNaissance(request.getDateNaissance());
        }
        if (request.getNiveauEtude() != null) {
            user.setNiveauEtude(request.getNiveauEtude());
        }
        if (request.getExperienceAnnees() != null) {
            validerExperienceAnnees(request.getExperienceAnnees());
            user.setExperienceAnnees(request.getExperienceAnnees());
        }
        if (request.getLinkedinUrl() != null) {
            user.setLinkedinUrl(request.getLinkedinUrl());
        }
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    public String changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec l'email " + email);
        }
        if (request.getOldPassword() == null || !passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect");
        }
        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("Le nouveau mot de passe est obligatoire");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Mot de passe modifié avec succès";
    }

    private User requerirUtilisateur(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id " + id));
    }

    private void verifierChampsObligatoires(UserRequest request) {
        if (request.getNom() == null || request.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (request.getPrenom() == null || request.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }
        if (request.getRole() == null) {
            throw new IllegalArgumentException("Le rôle est obligatoire");
        }
    }

    private void verifierEmailUnique(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("L'email existe déjà");
        }
    }

    private void validerExperienceAnnees(Integer experienceAnnees) {
        if (experienceAnnees < 0) {
            throw new IllegalArgumentException("Le nombre d'années d'expérience ne peut pas être négatif");
        }
    }
}