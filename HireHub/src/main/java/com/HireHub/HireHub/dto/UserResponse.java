package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.Role;

public record UserResponse(
        long id,
        String nom,
        String prenom,
        String email,
        Role role,
        boolean active) {
}