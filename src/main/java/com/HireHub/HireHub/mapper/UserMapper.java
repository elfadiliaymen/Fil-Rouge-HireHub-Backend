package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.dto.UserRequest;
import com.HireHub.HireHub.dto.UserResponse;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public final class UserMapper {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private UserMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setActive(user.isActive());
        response.setTelephone(user.getTelephone());
        response.setAdresse(user.getAdresse());
        response.setEntreprise(user.getEntreprise());
        response.setPoste(user.getPoste());
        response.setTelephonePro(user.getTelephonePro());
        response.setDateNaissance(user.getDateNaissance());
        response.setNiveauEtude(user.getNiveauEtude());
        response.setExperienceAnnees(user.getExperienceAnnees());
        response.setLinkedinUrl(user.getLinkedinUrl());
        return response;
    }

    public static User toUser(UserRequest request) {
        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPassword(PASSWORD_ENCODER.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.CANDIDAT);
        user.setActive(true);
        appliquerChampsProfil(user, request.getTelephone(), request.getAdresse(),
                request.getEntreprise(), request.getPoste(), request.getTelephonePro(),
                request.getDateNaissance(), request.getNiveauEtude(), request.getExperienceAnnees(),
                request.getLinkedinUrl());
        return user;
    }

    public static User toUser(RegisterRequest request) {
        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPassword(PASSWORD_ENCODER.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.CANDIDAT);
        user.setActive(true);
        appliquerChampsProfil(user, request.getTelephone(), request.getAdresse(),
                request.getEntreprise(), request.getPoste(), request.getTelephonePro(),
                request.getDateNaissance(), request.getNiveauEtude(), request.getExperienceAnnees(),
                request.getLinkedinUrl());
        return user;
    }

    public static UserRequest toUserRequest(RegisterRequest request) {
        UserRequest userRequest = new UserRequest();
        userRequest.setNom(request.getNom());
        userRequest.setPrenom(request.getPrenom());
        userRequest.setEmail(request.getEmail());
        userRequest.setPassword(request.getPassword());
        userRequest.setRole(request.getRole());
        userRequest.setTelephone(request.getTelephone());
        userRequest.setAdresse(request.getAdresse());
        userRequest.setEntreprise(request.getEntreprise());
        userRequest.setPoste(request.getPoste());
        userRequest.setTelephonePro(request.getTelephonePro());
        userRequest.setDateNaissance(request.getDateNaissance());
        userRequest.setNiveauEtude(request.getNiveauEtude());
        userRequest.setExperienceAnnees(request.getExperienceAnnees());
        userRequest.setLinkedinUrl(request.getLinkedinUrl());
        return userRequest;
    }

    private static void appliquerChampsProfil(User user, String telephone, String adresse,
                                              String entreprise, String poste, String telephonePro,
                                              LocalDate dateNaissance, String niveauEtude,
                                              Integer experienceAnnees, String linkedinUrl) {
        user.setTelephone(telephone);
        user.setAdresse(adresse);
        user.setEntreprise(entreprise);
        user.setPoste(poste);
        user.setTelephonePro(telephonePro);
        user.setDateNaissance(dateNaissance);
        user.setNiveauEtude(niveauEtude);
        user.setExperienceAnnees(experienceAnnees);
        user.setLinkedinUrl(linkedinUrl);
    }
}
