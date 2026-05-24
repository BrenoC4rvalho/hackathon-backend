package com.hackathon.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StudentRequest(
        @NotBlank String name,
        @NotBlank String registrationNumber,
        @NotBlank String phoneNumber,
        @NotNull LocalDate birthDate
) {
}