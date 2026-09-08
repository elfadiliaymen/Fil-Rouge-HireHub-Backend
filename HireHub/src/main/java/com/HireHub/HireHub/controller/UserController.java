package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import com.HireHub.HireHub.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public Page<User> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public User findByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/nom/{nom}")
    public User findByNom(@PathVariable String nom) {
        return userService.getUserByNom(nom);
    }

    @GetMapping("/role/{role}")
    public Page<User> findByRole(@PathVariable Role role, @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return userService.listerParRole(role, pageable);
    }

    @GetMapping("/stats")
    public Map<String, Long> statistiques() {
        return userService.statistiques();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.inscrire(user));
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.creerUtilisateur(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable long id, @RequestBody User user) {
        User misAJour = userService.updateUtilisateur(id, user);
        if (misAJour == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(misAJour);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<User> activate(@PathVariable long id) {
        User actived = userService.activer(id);
        if (actived == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actived);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivate(@PathVariable long id) {
        User desactive = userService.desactiver(id);
        if (desactive == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(desactive);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return userService.deleteUtilisateur(id);
    }
}