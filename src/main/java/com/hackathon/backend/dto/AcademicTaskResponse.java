package com.hackathon.backend.dto;

import com.hackathon.backend.enums.TaskType;

import java.time.LocalDateTime;

public record AcademicTaskResponse(
        Long id,
        String title,
        String description,
        TaskType type,
        LocalDateTime eventDate,
        LocalDateTime notificationDate,
        Long groupId,
        String groupName
) {
}
