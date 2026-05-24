package com.hackathon.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotNull Long taskId,
        @NotNull Long studentId,
        @NotBlank String message
) {
}
