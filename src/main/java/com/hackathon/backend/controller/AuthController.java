package com.hackathon.backend.controller;

import com.hackathon.backend.dto.AuthRequest;
import com.hackathon.backend.dto.AuthResponse;
import com.hackathon.backend.dto.RegisterRequest;
import com.hackathon.backend.entity.User;
import com.hackathon.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest request) {
        userService.register(request);
        return new AuthResponse("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthRequest request) {
        String token = userService.login(request);
        return new AuthResponse("Login realizado com sucesso", token);
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail()
        );
    }

    @PostMapping("/logout")
    public AuthResponse logout() {
        return new AuthResponse("Logout realizado com sucesso");
    }
}
