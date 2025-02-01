package com.example.codewavebe.common.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String errorCode;
    private String message;
    private List<String> errors;

    // factory method
    public static ErrorResponse of(String errorCode, String message, List<String> errors) {
        return new ErrorResponse(errorCode, message, errors);
    }

}