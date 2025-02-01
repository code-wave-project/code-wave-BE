package com.example.codewavebe.application;

import com.example.codewavebe.adapter.in.dto.CreateProjectRequest;
import com.example.codewavebe.adapter.in.dto.GetAllProjects;
import com.example.codewavebe.adapter.in.dto.GetProjectResponse;
import com.example.codewavebe.adapter.out.persistence.repository.ProjectRepository;
import com.example.codewavebe.adapter.out.persistence.repository.ProjectUserRepository;
import com.example.codewavebe.adapter.out.persistence.repository.UserRepository;
import com.example.codewavebe.common.dto.api.Message;
import com.example.codewavebe.domain.ide.Ide;
import com.example.codewavebe.domain.project.Project;
import com.example.codewavebe.domain.project.ProjectUser;
import com.example.codewavebe.domain.user.User;
import java.util.List;
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

    public GetAllProjects getAllProjects(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findAllByUser(user)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return GetAllProjects.of(projects);
    }

    @Transactional
    public Message deleteProject(String email, Long projectId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

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
