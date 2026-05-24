package com.hackathon.backend.controller;

import com.hackathon.backend.dto.NotificationRequest;
import com.hackathon.backend.dto.NotificationResponse;
import com.hackathon.backend.enums.NotificationStatus;
import com.hackathon.backend.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse create(@Valid @RequestBody NotificationRequest request) {
        return notificationService.create(request);
    }

    @GetMapping
    public List<NotificationResponse> findAll() {
        return notificationService.findAll();
    }

    @GetMapping("/{id}")
    public NotificationResponse findById(@PathVariable Long id) {
        return notificationService.findById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<NotificationResponse> findAllByStudentId(@PathVariable Long studentId) {
        return notificationService.findAllByStudentId(studentId);
    }

    @GetMapping("/task/{taskId}")
    public List<NotificationResponse> findAllByTaskId(@PathVariable Long taskId) {
        return notificationService.findAllByTaskId(taskId);
    }

    @GetMapping("/status/{status}")
    public List<NotificationResponse> findAllByStatus(@PathVariable NotificationStatus status) {
        return notificationService.findAllByStatus(status);
    }

    @PostMapping("/{id}/send")
    public NotificationResponse send(@PathVariable Long id) {
        return notificationService.send(id);
    }

    @PutMapping("/{id}")
    public NotificationResponse update(
            @PathVariable Long id,
            @Valid @RequestBody NotificationRequest request
    ) {
        return notificationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        notificationService.delete(id);
    }
}