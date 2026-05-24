package com.hackathon.backend.dto;

import com.hackathon.backend.enums.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AcademicTaskRequest(
        @NotBlank String title,
        String description,
        @NotNull TaskType type,
        @NotNull LocalDateTime eventDate,
        LocalDateTime notificationDate,
        @NotNull Long groupId
) {
}