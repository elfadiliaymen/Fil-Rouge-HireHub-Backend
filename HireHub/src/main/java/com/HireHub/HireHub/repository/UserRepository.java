package com.HireHub.HireHub.repository;

import com.HireHub.HireHub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByNom(String nom);

    User findById(long id);

}
