package com.example.codewavebe.adapter.out.persistence.repository;

import com.example.codewavebe.domain.ide.Ide;
import com.example.codewavebe.domain.project.Project;
import com.example.codewavebe.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT i FROM Ide i WHERE i.project.id = :projectId")
    List<Ide> findAllIdeById(Long projectId);

    @Query("SELECT p FROM Project p LEFT JOIN ProjectUser pu ON p.id = pu.project.id WHERE pu.user = :user")
    Optional<List<Project>> findAllByUser(User user);

    @Query("SELECT DISTINCT p " +
            "FROM Project p " +
            "JOIN ProjectUser pu ON p = pu.project " +
            "WHERE pu.user.email = :email")
    List<Project> findAllProjectsByUserEmail(String email);
}
