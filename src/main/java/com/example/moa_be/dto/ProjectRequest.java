package com.example.moa_be.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectRequest {
    private String title;
    private String description;
    private Long targetAmount;
    private String accountNumber;
    private LocalDate deadline;
    private List<Long> imageIds;
}
