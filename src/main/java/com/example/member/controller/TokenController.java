package com.example.member.controller;

import com.example.member.service.TokenService;
import com.example.member.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        String email = jwtUtil.validateToken(refreshToken);
        if (email == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid Refresh Token"));
        }

        String newAccessToken = jwtUtil.generateToken(email);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
