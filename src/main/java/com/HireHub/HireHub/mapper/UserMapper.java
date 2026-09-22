package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.dto.UserRequest;
import com.HireHub.HireHub.dto.UserResponse;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;

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
        return user;
    }

    public static User toUser(RegisterRequest request) {
        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.CANDIDAT);
        user.setActive(true);
        return user;
    }
}
