package com.example.moa_be.service;

import com.example.moa_be.db.Project;
import com.example.moa_be.db.ProjectImage;
import com.example.moa_be.db.Users;
import com.example.moa_be.dto.ProjectRequest;
import com.example.moa_be.dto.ProjectResponse;
import com.example.moa_be.repository.ProjectImageRepository;
import com.example.moa_be.repository.ProjectRepository;
import com.example.moa_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectImageRepository projectImageRepository;

    public ProjectResponse createProject(ProjectRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        Users user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setTargetAmount(request.getTargetAmount());
        project.setAccountNumber(request.getAccountNumber());
        project.setDeadline(request.getDeadline());
        project.setUsername(user.getUsername());
        project.setSchool(user.getSchool());

        projectRepository.save(project);

        if (request.getImageIds() != null && !request.getImageIds().isEmpty()) {
            for (Long imageId : request.getImageIds()) {
                ProjectImage image = projectImageRepository.findById(imageId)
                        .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다. ID: " + imageId));
                image.setProject(project); // ✅ 연결!
                projectImageRepository.save(image);
            }
        }

        return new ProjectResponse(
                project.getId(),
                project.getUsername(),
                project.getSchool(),
                project.getTitle(),
                project.getDescription(),
                project.getTargetAmount(),
                project.getCurrentAmount(),
                project.getAccountNumber(),
                project.getDeadline(),
                project.getImages().stream()
                        .map(ProjectImage::getFilePath)
                        .collect(Collectors.toList())
        );
    }

    public List<ProjectResponse> getProjects() {
        return projectRepository.findAll().stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getUsername(),
                        project.getSchool(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getTargetAmount(),
                        project.getCurrentAmount(),
                        project.getAccountNumber(),
                        project.getDeadline(),
                        project.getImages().stream()
                                .map(ProjectImage::getFilePath)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<ProjectResponse> getProjectsByMySchool() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        Users user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String school = user.getSchool();

        return projectRepository.findBySchool(school).stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getUsername(),
                        project.getSchool(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getTargetAmount(),
                        project.getCurrentAmount(),
                        project.getAccountNumber(),
                        project.getDeadline(),
                        project.getImages().stream()
                                .map(ProjectImage::getFilePath)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<ProjectResponse> getProjectsByOtherSchools() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        Users user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String school = user.getSchool();

        return projectRepository.findBySchoolNot(school).stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getUsername(),
                        project.getSchool(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getTargetAmount(),
                        project.getCurrentAmount(),
                        project.getAccountNumber(),
                        project.getDeadline(),
                        project.getImages().stream()
                                .map(ProjectImage::getFilePath)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public ProjectResponse getProjectDetail(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("프로젝트 없음"));

        return new ProjectResponse(
                project.getId(),
                project.getUsername(),
                project.getSchool(),
                project.getTitle(),
                project.getDescription(),
                project.getTargetAmount(),
                project.getCurrentAmount(),
                project.getAccountNumber(),
                project.getDeadline(),
                project.getImages().stream()
                        .map(ProjectImage::getFilePath)
                        .collect(Collectors.toList())
        );
    }
}
