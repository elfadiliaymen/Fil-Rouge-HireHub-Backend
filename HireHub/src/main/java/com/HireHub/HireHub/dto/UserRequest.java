package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.Role;

public record UserRequest(
        String nom,
        String prenom,
        String email,
        String password,
        Role role) {
}