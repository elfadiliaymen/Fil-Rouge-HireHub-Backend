package com.HireHub.HireHub.service;

import com.HireHub.HireHub.config.JwtUtils;
import com.HireHub.HireHub.dto.LoginRequest;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String EMAIL = "candidat@example.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    @Test
    void loginValidePasseParAuthenticationManagerEtEmetLeToken() {
        // Arrange
        LoginRequest request = loginRequest(EMAIL, "secret");
        User user = new User();
        user.setId(7L);
        user.setEmail(EMAIL);
        user.setRole(Role.CANDIDAT);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(EMAIL, "secret"));
        when(userRepository.findByEmail(EMAIL)).thenReturn(user);
        when(jwtUtils.generateToken(7L, EMAIL, "CANDIDAT")).thenReturn("token");

        // Act
        String result = authService.login(request);

        // Assert
        assertEquals("token", result);
    }
}
