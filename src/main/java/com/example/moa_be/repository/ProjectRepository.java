package com.example.moa_be.repository;

import com.example.moa_be.db.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findBySchool(String school);
    List<Project> findBySchoolNot(String school);
    Optional<Project> findById(Long id);
}
