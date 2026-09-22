package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.dto.ChangePasswordRequest;
import com.HireHub.HireHub.dto.UpdateProfileRequest;
import com.HireHub.HireHub.dto.UserResponse;
import com.HireHub.HireHub.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final UserService userService;

    public MeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserResponse me() {
        return userService.getMe(currentEmail());
    }

    @PutMapping
    public UserResponse update(@RequestBody UpdateProfileRequest request) {
        return userService.updateMe(currentEmail(), request);
    }

    @PostMapping("/password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.changePassword(currentEmail(), request);
    }

    private String currentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié");
        }
        return authentication.getName();
    }
}