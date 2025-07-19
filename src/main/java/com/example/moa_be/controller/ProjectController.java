package com.example.moa_be.controller;

import com.example.moa_be.dto.ProjectRequest;
import com.example.moa_be.dto.ProjectResponse;
import com.example.moa_be.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ProjectResponse createProject(@RequestBody ProjectRequest request) {
        return projectService.createProject(request);
    }

    @GetMapping("/list")
    public List<ProjectResponse> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/list/myschool")
    public List<ProjectResponse> getProjectsByMySchool() {
        return projectService.getProjectsByMySchool();
    }

    @GetMapping("/list/otherschool")
    public List<ProjectResponse> getProjectsByOtherSchools() {
        return projectService.getProjectsByOtherSchools();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProjectResponse> getProjectDetail(@PathVariable Long id) {
        ProjectResponse response = projectService.getProjectDetail(id);
        return ResponseEntity.ok(response);
    }
}
