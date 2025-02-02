package com.example.codewavebe.adapter.in;

import com.example.codewavebe.adapter.in.dto.ForgotPasswordRequest;
import com.example.codewavebe.adapter.in.dto.MyInfoResponse;
import com.example.codewavebe.adapter.in.dto.ResetPasswordRequest;
import com.example.codewavebe.adapter.in.dto.SignInRequest;
import com.example.codewavebe.adapter.in.dto.SignInResponse;
import com.example.codewavebe.adapter.in.dto.SignUpRequest;
import com.example.codewavebe.application.UserService;
import com.example.codewavebe.common.dto.api.ApiResponse;
import com.example.codewavebe.common.dto.api.Message;
import com.example.codewavebe.util.helper.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authorization", description = "Authorization API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "회원가입", description = "신규 사용자가 회원가입을 요청합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Message>> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(ApiResponse.success(userService.signUp(signUpRequest)));
    }

    @Operation(summary = "로그인", description = "사용자가 로그인을 요청합니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@RequestBody SignInRequest signInRequest) {
        System.out.println("signInRequest.email() = " + signInRequest.email());
        return ResponseEntity.ok(ApiResponse.success(userService.signIn(signInRequest)));
    }

    @Operation(summary = "비밀번호 재설정 요청", description = "사용자가 비밀번호 재설정을 요청합니다.")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Message>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.forgotPassword(request.email())));
    }

    @Operation(summary = "비밀번호 재설정", description = "사용자가 새로운 비밀번호를 설정합니다.")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Message>> resetPassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ResetPasswordRequest request) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        System.out.println("jwt = " + jwt);
        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Invalid token");
        }
        String email = jwtUtil.extractId(jwt);
        System.out.println("userId = " + email);
        return ResponseEntity.ok(ApiResponse.success(userService.resetPassword(email, request)));
    }

    @Operation(summary = "내 정보 조회", description = "토큰으로 내 정보를 조회합니다.")
    @GetMapping("/myInfo")
    public ResponseEntity<ApiResponse<MyInfoResponse>> whoAmI(
            @RequestHeader("Authorization") String token
    ) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        System.out.println("jwt = " + jwt);
        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Invalid token");
        }
        String email = jwtUtil.extractId(jwt);
        System.out.println("userId = " + email);
        return ResponseEntity.ok(ApiResponse.success(userService.whoAmI(email)));
    }
}