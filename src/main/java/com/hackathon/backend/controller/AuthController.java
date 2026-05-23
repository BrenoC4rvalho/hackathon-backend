package com.hackathon.backend.controller;

import com.hackathon.backend.dto.AuthRequest;
import com.hackathon.backend.dto.AuthResponse;
import com.hackathon.backend.dto.RegisterRequest;
import com.hackathon.backend.entity.User;
import com.hackathon.backend.service.UserService;
import jakarta.servlet.http.Cookie;
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
    public AuthResponse login(
            @RequestBody @Valid AuthRequest request,
            HttpServletResponse response
    ) {
        String token = userService.login(request);

        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true em produção com HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        return new AuthResponse("Login realizado com sucesso");
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
    public AuthResponse logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return new AuthResponse("Logout realizado com sucesso");
    }
}