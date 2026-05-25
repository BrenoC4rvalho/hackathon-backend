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

        // SameSite=None necessário para cross-site (Vercel + Render)
        response.setHeader("Set-Cookie",
                "access_token=" + token
                + "; Path=/"
                + "; HttpOnly"
                + "; Secure"
                + "; SameSite=None"
                + "; Max-Age=" + (24 * 60 * 60)
        );

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
        response.setHeader("Set-Cookie",
                "access_token="
                + "; Path=/"
                + "; HttpOnly"
                + "; Secure"
                + "; SameSite=None"
                + "; Max-Age=0"
        );

        return new AuthResponse("Logout realizado com sucesso");
    }
}
