package com.example.codewavebe.adapter.in.dto;

import com.example.codewavebe.domain.project.Project;
import java.util.List;

public record GetAllProjects(
        List<Project> projects
) {
    public static GetAllProjects of(List<Project> projects) {
        return new GetAllProjects(projects);
    }
}
