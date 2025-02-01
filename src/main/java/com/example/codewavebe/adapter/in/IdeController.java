package com.example.codewavebe.adapter.in;

import com.example.codewavebe.adapter.in.dto.IdeRequestDto;
import com.example.codewavebe.adapter.in.dto.IdeResponseDto;
import com.example.codewavebe.application.IdeService;
import com.example.codewavebe.common.dto.api.ApiResponse;
import com.example.codewavebe.common.dto.api.Message;
import com.example.codewavebe.util.helper.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ide", description = "IDE API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ide")
public class IdeController {

    private final IdeService ideService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "코드 저장", description = "작성한 코드를 저장합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<IdeResponseDto>> saveCode(
            @RequestHeader("Authorization") String token,
            @RequestBody IdeRequestDto requestDto
    ) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String email = jwtUtil.extractId(jwt);
        return ResponseEntity.ok(ApiResponse.success(ideService.saveCode(email, requestDto)));
    }

    @Operation(summary = "특정 소스코드 조회", description = "특정 소스코드를 조회합니다.")
    @GetMapping("/{ideId}")
    public ResponseEntity<ApiResponse<IdeResponseDto>> getCode(@PathVariable Long ideId) {
        return ResponseEntity.ok(ApiResponse.success(ideService.getCode(ideId)));
    }

    @Operation(summary = "특정 소스코드 수정", description = "특정 소스코드를 수정합니다.")
    @PatchMapping("/{ideId}")
    public ResponseEntity<ApiResponse<Message>> updateCode(
            @PathVariable Long ideId,
            @RequestBody IdeRequestDto requestDto
    ) {
        return ResponseEntity.ok(ApiResponse.success(ideService.updateCode(ideId, requestDto)));
    }

    @Operation(summary = "특정 소스코드 삭제", description = "특정 소스코드 데이터를 삭제합니다.")
    @DeleteMapping("/{ideId}")
    public ResponseEntity<ApiResponse<Message>> deleteCode(@PathVariable Long ideId) {
        ideService.deleteCode(ideId);
        return ResponseEntity.ok(ApiResponse.success(Message.builder().message("삭제가 완료되었습니다.").build()));
    }
}
