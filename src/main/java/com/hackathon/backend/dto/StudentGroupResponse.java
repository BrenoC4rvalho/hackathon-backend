package com.hackathon.backend.dto;

public record StudentGroupResponse(
        Long id,
        Long studentId,
        String studentName,
        Long groupId,
        String groupName
) {
}