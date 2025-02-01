package com.example.codewavebe.domain.project;

import com.example.codewavebe.common.BaseEntity;
import com.example.codewavebe.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String title;

    private String description;

    private String Initiator;

    private String inviteCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Project(String title, String description, User user) {
        this.title = title;
        this.description = description;
        Initiator = user.getUsername();
        this.inviteCode = UUID.randomUUID().toString();
        this.user = user;
    }

    public static Project of(String title, String description, User user) {
        return new Project(title, description, user);
    }

    public void updateProject(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
