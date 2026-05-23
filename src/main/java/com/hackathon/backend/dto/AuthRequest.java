package com.hackathon.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @Email
    private String email;

    @NotBlank
    private String password;
}