package com.example.moa_be.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String userid;
    private String username;
    private String password;
    private String email;
    private String school;
    private String studentNumber;
    private String role;
}
