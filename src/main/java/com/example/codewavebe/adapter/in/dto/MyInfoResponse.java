package com.example.codewavebe.adapter.in.dto;

import com.example.codewavebe.domain.user.User;

public record MyInfoResponse(
        Long userId,
        String username,
        String email
) {
    public MyInfoResponse(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public static MyInfoResponse of(User user) {
        return new MyInfoResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
