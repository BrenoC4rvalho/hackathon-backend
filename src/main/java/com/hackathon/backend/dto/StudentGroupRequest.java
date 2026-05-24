package com.hackathon.backend.dto;

import jakarta.validation.constraints.NotNull;

public record StudentGroupRequest(
        @NotNull Long studentId,
        @NotNull Long groupId
) {
}