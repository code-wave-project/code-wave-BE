package com.example.codewavebe.adapter.out.persistence.repository;

import com.example.codewavebe.domain.project.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
}
