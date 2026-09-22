package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.dto.UserRequest;
import com.HireHub.HireHub.dto.UserResponse;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;

import java.time.LocalDate;

public final class UserMapper {

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
        user.setPassword(request.getPassword());
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
        user.setPassword(request.getPassword());
        user.setRole(request.getRole() != null ? request.getRole() : Role.CANDIDAT);
        user.setActive(true);
        appliquerChampsProfil(user, request.getTelephone(), request.getAdresse(),
                request.getEntreprise(), request.getPoste(), request.getTelephonePro(),
                request.getDateNaissance(), request.getNiveauEtude(), request.getExperienceAnnees(),
                request.getLinkedinUrl());
        return user;
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
