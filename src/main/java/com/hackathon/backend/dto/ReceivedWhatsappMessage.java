package com.hackathon.backend.dto;

public record ReceivedWhatsappMessage(
        String from,
        String to,
        String body,
        String messageSid
) {}