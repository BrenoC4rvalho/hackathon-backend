package com.hackathon.backend.dto;

import com.hackathon.backend.entity.TaskType;

import java.time.LocalDate;

public record AcademicTaskResponse(
        Long id,
        String title,
        String description,
        TaskType type,
        LocalDate dueDate,
        Long groupId,
        String groupName
) {
}
