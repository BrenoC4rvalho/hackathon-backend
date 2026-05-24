package com.hackathon.backend.dto;

import com.hackathon.backend.entity.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AcademicTaskRequest(
        @NotBlank String title,
        String description,
        @NotNull TaskType type,
        @NotNull LocalDate dueDate,
        @NotNull Long groupId
) {
}
