package com.example.codewavebe.adapter.in.dto;

import com.example.codewavebe.domain.ide.Ide;
import lombok.Getter;

@Getter
public class IdeResponseDto {
    private Long id;
    private String code;
    private Long userId;

    private IdeResponseDto(Long id, String code, Long userId) {
        this.id = id;
        this.code = code;
        this.userId = userId;
    }

    public static IdeResponseDto from(Ide ide) {
        return new IdeResponseDto(ide.getId(), ide.getCode(), ide.getUser().getId());
    }
}