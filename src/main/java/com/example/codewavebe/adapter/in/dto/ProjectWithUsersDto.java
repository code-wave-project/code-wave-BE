package com.example.codewavebe.adapter.in.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectWithUsersDto(
        Long projectId,
        String title,
        String description,
        String initiator,
        String inviteCode,
        List<UserDto> users,
        LocalDateTime createdAt
) {
    public record UserDto(
            Long id,
            String email,
            String username
    ) { }
}
