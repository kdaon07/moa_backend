package com.example.moa_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String username;
    private String school;
    private String title;
    private String description;
    private Long targetAmount;
    private Long currentAmount;
    private String accountNumber;
    private LocalDate deadline;
    private List<String> imagePaths;
}
