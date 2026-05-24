package com.hackathon.backend.dto;

public record SendWhatsappResponse(
        String sid,
        String status,
        String to
) {}