package com.HireHub.HireHub.dto;

public record RegisterRequest(
        String nom,
        String prenom,
        String email,
        String password) {
}