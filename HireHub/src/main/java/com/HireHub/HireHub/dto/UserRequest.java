package com.HireHub.HireHub.dto;

import com.HireHub.HireHub.entity.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Role role;
}