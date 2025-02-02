package com.example.codewavebe.adapter.in.dto;

import com.example.codewavebe.domain.project.Project;
import com.example.codewavebe.domain.user.User;
import java.util.List;

public record GetAllProjects(
        List<Project> projects,
        List<User> users
) {
    public static GetAllProjects of(List<Project> projects, List<User> users) {
        return new GetAllProjects(projects, users);
    }
}
