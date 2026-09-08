package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByNom(String nom);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    long countByRole(Role role);
}