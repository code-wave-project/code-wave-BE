package com.example.codewavebe.adapter.in.dto;

public record SignUpRequest(
        String email,
        String password,
        String name
) {
}