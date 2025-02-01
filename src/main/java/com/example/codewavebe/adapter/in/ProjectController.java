package com.example.codewavebe.adapter.in;

import com.example.codewavebe.adapter.in.dto.CreateProjectRequest;
import com.example.codewavebe.application.ProjectService;
import com.example.codewavebe.common.dto.api.ApiResponse;
import com.example.codewavebe.common.dto.api.Message;
import com.example.codewavebe.util.helper.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Project", description = "Project API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "프로젝트 상세 조회", description = "프로젝트를 상세조회 합니다.")
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<?>> getProject(
            @RequestHeader("Authorization") String token,
            @PathVariable Long projectId
    ) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String email = jwtUtil.extractId(jwt);
        return ResponseEntity.ok(ApiResponse.success(projectService.getProject(email, projectId)));
    }

    @Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Message>> createProject(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateProjectRequest createProjectRequest
    ) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String email = jwtUtil.extractId(jwt);
        return ResponseEntity.ok(ApiResponse.success(projectService.createProject(email, createProjectRequest)));
    }

    @Operation(summary = "프로젝트 수정", description = "선택한 프로젝트를 수정합니다.")
    @PatchMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Message>> editProject(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateProjectRequest createProjectRequest,
            @PathVariable Long projectId
    ) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String email = jwtUtil.extractId(jwt);
        return ResponseEntity.ok(ApiResponse.success(projectService.editProject(email, createProjectRequest, projectId)));
    }

    @Operation(summary = "전체 프로젝트 조회", description = "접근 권한이 있는 전체 프로젝트를 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllProjects(
            @RequestHeader("Authorization") String token
    ) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String email = jwtUtil.extractId(jwt);
        return ResponseEntity.ok(ApiResponse.success(projectService.getAllProjects(email)));
    }

    @Operation(summary = "프로젝트 삭제", description = "선택한 프로젝트를 삭제합니다.")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Message>> deleteProject(
            @RequestHeader("Authorization") String token,
            @PathVariable Long projectId
    ) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtUtil.validateToken(jwt)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String email = jwtUtil.extractId(jwt);
        return ResponseEntity.ok(ApiResponse.success(projectService.deleteProject(email, projectId)));
    }
}
