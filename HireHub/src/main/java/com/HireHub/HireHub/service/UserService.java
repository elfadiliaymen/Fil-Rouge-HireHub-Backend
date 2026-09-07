package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);

    }
    public User getUserByNom(String nom){
        return userRepository.findByNom(nom);
    }

    public User getUserById(long id){
        return userRepository.findById(id);
    }
}
