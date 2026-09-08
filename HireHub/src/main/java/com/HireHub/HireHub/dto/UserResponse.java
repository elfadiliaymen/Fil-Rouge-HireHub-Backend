package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private long id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private boolean active;
}