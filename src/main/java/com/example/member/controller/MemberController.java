package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import com.example.member.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController // RESTful API
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil; // JWT 토큰 관련 유틸리티 클래스

    // 회원가입 처리
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody MemberDTO memberDTO) {
        memberService.save(memberDTO); // 회원 정보를 저장하는 서비스 호출

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "회원가입 성공!");
        response.put("user", memberDTO.getEmail()); // 이메일을 반환하거나 다른 사용자 정보를 추가 가능

        return ResponseEntity.status(201).body(response); // 201 Created 응답
    }

    // 로그인 처리 (JWT 발급)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO) {
        boolean isAuthenticated = memberService.authenticate(memberDTO.getEmail(), memberDTO.getPassword());

        if (isAuthenticated) {
            String token = jwtUtil.generateToken(memberDTO.getEmail()); // JWT 토큰 생성
            return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
        } else {
            return ResponseEntity.status(401).body("{\"error\":\"Invalid credentials\"}");
        }
    }

    // 이메일 중복 체크
    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam("email") String email) {
        boolean isDuplicate = memberService.checkEmailDuplicate(email);

        if (isDuplicate) {
            return ResponseEntity.status(409).body("이미 사용 중인 이메일입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        }
    }

    // 보호된 엔드포인트: 사용자 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(@RequestHeader("Authorization") String token) {
        // "Bearer " 접두사를 제거하고 토큰 검증
        String email = jwtUtil.validateToken(token.replace("Bearer ", ""));

        if (email != null) {
            return ResponseEntity.ok("프로필 조회 성공! 사용자 이메일: " + email);
        } else {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }
    }

    // 비밀번호 재설정 요청
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");  // 요청 본문에서 email을 가져옵니다.
        boolean isEmailSent = memberService.sendPasswordResetToken(email);  // 이메일로 재설정 링크 전송

        Map<String, String> response = new HashMap<>();
        if (isEmailSent) {
            response.put("status", "success");
            response.put("message", "비밀번호 재설정 링크를 이메일로 보냈습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "등록되지 않은 이메일입니다.");
            return ResponseEntity.status(404).body(response);
        }
    }

    // 비밀번호 재설정 완료
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean isPasswordReset = memberService.resetPassword(token, newPassword);

        Map<String, String> response = new HashMap<>();
        if (isPasswordReset) {
            response.put("status", "success");
            response.put("message", "비밀번호가 성공적으로 재설정되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "유효하지 않은 토큰입니다.");
            return ResponseEntity.status(400).body(response);
        }
    }

    // 아이디 찾기
    @PostMapping("/find-username")
    public ResponseEntity<Map<String, String>> findUsername(@RequestBody Map<String, String> request) {
        String email = request.get("email"); // 요청에서 이메일 가져오기

        String username = memberService.findUsernameByEmail(email); // 이메일로 아이디 찾기

        Map<String, String> response = new HashMap<>();
        if (username != null) {
            // 아이디를 찾았을 경우
            response.put("status", "success");
            response.put("message", "아이디 찾기가 완료되었습니다.");
            response.put("username", username); // 아이디 반환
            return ResponseEntity.ok(response);
        } else {
            // 이메일이 등록되어 있지 않을 경우
            response.put("status", "error");
            response.put("message", "해당 이메일로 등록된 계정을 찾을 수 없습니다.");
            return ResponseEntity.status(404).body(response);
        }
    }
}
