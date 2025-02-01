package com.example.codewavebe.adapter.in.dto;

import com.example.codewavebe.domain.ide.Ide;
import java.util.List;

public record GetProjectResponse(
        List<Ide> ides
) {

    public static GetProjectResponse of(List<Ide> ides) {
        return new GetProjectResponse(ides);
    }
}
