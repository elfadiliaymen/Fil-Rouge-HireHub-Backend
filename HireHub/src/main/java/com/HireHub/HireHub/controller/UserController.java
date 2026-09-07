package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{email}")
    public User consulterUserParemail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }
    @GetMapping("/{nom}")
    public User consulterUserParnom(@PathVariable String nom){
        return userService.getUserByNom(nom);
    }

    @GetMapping("/{id}")
    public User consulterUserParId(@PathVariable Long id){
        return userService.getUserById(id);
    }

}
