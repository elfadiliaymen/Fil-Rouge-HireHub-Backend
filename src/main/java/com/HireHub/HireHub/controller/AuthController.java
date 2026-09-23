package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.config.JwtUtils;
import com.HireHub.HireHub.config.TokenBlacklist;
import com.HireHub.HireHub.dto.LoginRequest;
import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final JwtUtils jwtUtils;

    private final TokenBlacklist tokenBlacklist;

    public AuthController(AuthService authService, JwtUtils jwtUtils, TokenBlacklist tokenBlacklist) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.tokenBlacklist = tokenBlacklist;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtUtils.validateToken(token)) {
                tokenBlacklist.add(token, jwtUtils.extractExpiration(token).getTime());
            }
        }
        return ResponseEntity.ok("Déconnexion réussie");
    }
}