package com.example.moa_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyInfoResponse {
    private String username;
    private String school;
    private String studentNumber;
    private String role;
}
