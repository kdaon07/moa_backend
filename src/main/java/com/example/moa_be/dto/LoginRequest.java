package com.example.moa_be.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userid;
    private String password;
}
