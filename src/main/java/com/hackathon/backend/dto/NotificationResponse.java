package com.hackathon.backend.dto;

import com.hackathon.backend.entity.NotificationStatus;

public record NotificationResponse(
        Long id,
        String studentName,
        String phone,
        String message,
        NotificationStatus status,
        String taskTitle,
        String createdAt
) {
}
