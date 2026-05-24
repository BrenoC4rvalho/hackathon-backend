package com.hackathon.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record SendWhatsappRequest(
        @NotBlank String to,
        @NotBlank String message
) {}