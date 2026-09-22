package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.dto.RegisterRequest;
import com.HireHub.HireHub.dto.UserRequest;
import com.HireHub.HireHub.dto.UserResponse;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<UserResponse> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public UserResponse findByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nom/{nom}")
    public UserResponse findByNom(@PathVariable String nom) {
        return userService.getUserByNom(nom);
    }

    @GetMapping("/role/{role}")
    public Page<UserResponse> findByRole(@PathVariable Role role, @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return userService.listerParRole(role, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public Map<String, Long> statistiques() {
        return userService.statistiques();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.inscrire(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.creerUtilisateur(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable long id, @RequestBody UserRequest request) {
        return userService.updateUtilisateur(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public UserResponse activate(@PathVariable long id) {
        return userService.activer(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public UserResponse deactivate(@PathVariable long id) {
        return userService.desactiver(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return userService.deleteUtilisateur(id);
    }
}