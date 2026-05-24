package com.hackathon.backend.controller;

import com.hackathon.backend.dto.NotificationResponse;
import com.hackathon.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> findAll() {
        return notificationService.findAll();
    }

    @PostMapping("/{id}/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void send(@PathVariable Long id) {
        notificationService.send(id);
    }

    @PostMapping("/resend-errors")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resendErrors() {
        notificationService.resendErrors();
    }
}
