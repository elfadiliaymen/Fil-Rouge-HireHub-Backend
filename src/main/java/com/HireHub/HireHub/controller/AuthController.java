package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.config.JwtFilter;
import com.HireHub.HireHub.config.JwtUtils;
import com.HireHub.HireHub.dto.LoginRequest;
import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final JwtUtils jwtUtils;

    private final boolean cookieSecure;

    public AuthController(AuthService authService,
                          JwtUtils jwtUtils,
                          @Value("${jwt.cookie-secure:false}") boolean cookieSecure) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.cookieSecure = cookieSecure;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        String token = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookieDeSession(token).toString())
                .body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieDeSession(token).toString())
                .body(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieDeSessionExpiree().toString())
                .body("Déconnexion réussie");
    }

    private ResponseCookie cookieDeSession(String token) {
        Date expiration = jwtUtils.extractExpiration(token);
        long maxAgeSec = Math.max((expiration.getTime() - System.currentTimeMillis()) / 1000, 0);
        return ResponseCookie.from(JwtFilter.AUTH_COOKIE, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAgeSec)
                .build();
    }

    private ResponseCookie cookieDeSessionExpiree() {
        return ResponseCookie.from(JwtFilter.AUTH_COOKIE, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
    }
}
