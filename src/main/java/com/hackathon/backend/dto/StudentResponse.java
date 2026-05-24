package com.hackathon.backend.dto;

import java.time.LocalDate;

public record StudentResponse(
        Long id,
        String name,
        String registrationNumber,
        String phoneNumber,
        LocalDate birthDate
) {
}