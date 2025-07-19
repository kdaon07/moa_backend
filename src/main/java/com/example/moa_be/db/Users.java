package com.example.moa_be.db;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userid;
    private String username;
    private String password;
    private String email;
    private String school;
    private String studentNumber;
    private String role;
}