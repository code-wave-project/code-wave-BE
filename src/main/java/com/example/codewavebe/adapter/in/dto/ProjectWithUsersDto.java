package com.example.codewavebe.adapter.in.dto;

import java.util.List;

public record ProjectWithUsersDto(
        Long projectId,
        String title,
        String description,
        String initiator,
        String inviteCode,
        List<UserDto> users
) {
    public record UserDto(
            Long id,
            String email,
            String username
    ) { }
}
