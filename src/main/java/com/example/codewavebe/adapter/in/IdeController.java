package com.example.codewavebe.adapter.in;

import com.example.codewavebe.adapter.in.dto.IdeRequestDto;
import com.example.codewavebe.adapter.in.dto.IdeResponseDto;
import com.example.codewavebe.application.IdeService;
import com.example.codewavebe.common.dto.api.ApiResponse;
import com.example.codewavebe.common.dto.api.Message;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ide", description = "IDE API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ide")
public class IdeController {

    private final IdeService ideService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<IdeResponseDto>> saveCode(
            @PathVariable Long userId,
            @RequestBody IdeRequestDto requestDto
    ) {
        return ResponseEntity.ok(ApiResponse.success(ideService.saveCode(userId, requestDto)));
    }

    // 특정 코드 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IdeResponseDto>> getCode(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(ideService.getCode(id)));
    }

    // 사용자의 모든 코드 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<IdeResponseDto>>> getUserCodes(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(ideService.getUserCodes(userId)));
    }

    // 코드 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<IdeResponseDto>> updateCode(
            @PathVariable Long id,
            @RequestBody IdeRequestDto requestDto
    ) {
        return ResponseEntity.ok(ApiResponse.success(ideService.updateCode(id, requestDto)));
    }

    // 코드 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Message>> deleteCode(@PathVariable Long id) {
        ideService.deleteCode(id);
        return ResponseEntity.ok(ApiResponse.success(Message.builder().message("삭제가 완료되었습니다.").build()));
    }
}
