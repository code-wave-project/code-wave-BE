package com.example.codewavebe.adapter.in.dto;

public record ResetPasswordRequest(
        String codeNumber,
        String newPassword
) {
}
