package com.example.codewavebe.domain.user;

import com.example.codewavebe.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String email;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String code;

    public User(String email, String password, String username) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = Role.USER;
    }

    public static User of(String email, String password, String username) {
        return new User(email, password, username);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateCode(String code) {
        this.code = code;
    }
}