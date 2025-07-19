package com.example.moa_be.db;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProjectImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    @ManyToOne
    private Project project;
}
