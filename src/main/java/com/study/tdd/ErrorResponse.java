package com.study.tdd;

public record ErrorResponse(
        String code,
        String message
) {
}
