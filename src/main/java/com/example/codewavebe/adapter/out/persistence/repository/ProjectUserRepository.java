package com.example.codewavebe.adapter.out.persistence.repository;

import com.example.codewavebe.domain.project.Project;
import com.example.codewavebe.domain.project.ProjectUser;
import com.example.codewavebe.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    @Query("SELECT DISTINCT pu.user " +
            "FROM ProjectUser pu " +
            "WHERE pu.project IN (" +
            "   SELECT p " +
            "   FROM Project p " +
            "   JOIN ProjectUser pu2 ON p = pu2.project " +
            "   WHERE pu2.user.email = :email" +
            ")")
    List<User> findAllUsersInProjectsByUserEmail(String email);

    @Query("SELECT DISTINCT pu.project " +
            "FROM ProjectUser pu " +
            "WHERE pu.user.email = :email")
    List<Project> findProjectsByUserEmail(@Param("email") String email);

    // 특정 프로젝트에 속한 사용자 목록 조회
    @Query("SELECT pu.user " +
            "FROM ProjectUser pu " +
            "WHERE pu.project.id = :projectId")
    List<User> findUsersByProjectId(@Param("projectId") Long projectId);
}
