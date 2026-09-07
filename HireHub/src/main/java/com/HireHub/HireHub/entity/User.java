package com.HireHub.HireHub.entity;


import com.HireHub.HireHub.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false , length = 20)
    private String nom;
    @Column(nullable = false , length = 20)
    private String prenom;
    @Column(nullable = false ,unique = true, length = 100)
    private String email;
    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean active;



}

