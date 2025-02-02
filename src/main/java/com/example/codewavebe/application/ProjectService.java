package com.example.codewavebe.application;

import com.example.codewavebe.adapter.in.dto.CreateProjectRequest;
import com.example.codewavebe.adapter.in.dto.GetAllProjectsResponse;
import com.example.codewavebe.adapter.in.dto.GetProjectResponse;
import com.example.codewavebe.adapter.in.dto.ProjectWithUsersDto;
import com.example.codewavebe.adapter.in.dto.ProjectWithUsersDto.UserDto;
import com.example.codewavebe.adapter.out.persistence.repository.ProjectRepository;
import com.example.codewavebe.adapter.out.persistence.repository.ProjectUserRepository;
import com.example.codewavebe.adapter.out.persistence.repository.UserRepository;
import com.example.codewavebe.common.dto.api.Message;
import com.example.codewavebe.domain.ide.Ide;
import com.example.codewavebe.domain.project.Project;
import com.example.codewavebe.domain.project.ProjectUser;
import com.example.codewavebe.domain.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

    public GetProjectResponse getProject(String email, Long projectId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Ide> allIdeById = projectRepository.findAllIdeById(projectId);

        return GetProjectResponse.of(allIdeById);
    }

    @Transactional
    public Message createProject(String email, CreateProjectRequest createProjectRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.of(createProjectRequest.projectName(), createProjectRequest.projectDescription(),
                user);

        projectRepository.save(project);

        ProjectUser projectUser = ProjectUser.builder()
                .project(project)
                .user(user)
                .isOwner(true)
                .build();
        projectUserRepository.save(projectUser);

        return Message.builder().message("프로젝트가 생성되었습니다.").build();
    }

    @Transactional
    public Message editProject(String email, CreateProjectRequest createProjectRequest, Long projectId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.updateProject(createProjectRequest.projectName(), createProjectRequest.projectDescription());
        return Message.builder().message("프로젝트를 수정했습니다.").build();
    }

    public GetAllProjectsResponse getAllProjects(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectUserRepository.findProjectsByUserEmail(email);
        if (projects.isEmpty()) {
            throw new RuntimeException("No projects found for user");
        }

        // 각 프로젝트별로 소속된 사용자 목록 조회 및 DTO 매핑
        List<ProjectWithUsersDto> projectDtos = projects.stream().map(project -> {
            List<User> projectUsers = projectUserRepository.findUsersByProjectId(project.getId());
            List<UserDto> userDtos = projectUsers.stream()
                    .map(u -> new UserDto(u.getId(), u.getEmail(), u.getUsername()))
                    .collect(Collectors.toList());

            return new ProjectWithUsersDto(
                    project.getId(),
                    project.getTitle(),
                    project.getDescription(),
                    project.getInitiator(), // 엔티티의 필드명이 "Initiator"라면 그대로 사용
                    project.getInviteCode(),
                    userDtos,
                    project.getCreatedAt()
            );
        }).collect(Collectors.toList());

        // 전체 결과를 하나의 래핑 DTO로 반환
        return new GetAllProjectsResponse(projectDtos);
    }

    @Transactional
    public Message deleteProject(String email, Long projectId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        projectUserRepository.deleteByProjectId(projectId);
        projectRepository.delete(project);
        return Message.builder().message(projectId.toString() + "번 프로젝트를 삭제했습니다.").build();
    }

    @Transactional
    public Message joinProject(String email, Long projectId, String inviteCode) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getInviteCode().equals(inviteCode)) {
            return Message.builder().message("초대 코드가 일치하지 않습니다.").build();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        projectUserRepository.save(new ProjectUser(project, user, false));

        return Message.builder().message("선택한 프로젝트에 참여했습니다.").build();
    }
}
