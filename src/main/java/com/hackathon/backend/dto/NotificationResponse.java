package com.hackathon.backend.dto;

import com.hackathon.backend.enums.NotificationStatus;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        Long taskId,
        String taskTitle,
        Long studentId,
        String studentName,
        String phoneNumber,
        String message,
        NotificationStatus status,
        LocalDateTime sentAt,
        String errorMessage
) {
}