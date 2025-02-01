package com.example.codewavebe.adapter.in.dto;

import com.example.codewavebe.domain.user.Role;
import lombok.Builder;

public record SignInResponse(
        String accessToken,
        String tokenType,
        Role role
) {

    @Builder
    public SignInResponse(String accessToken, String tokenType, Role role) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.role = role;
    }
}