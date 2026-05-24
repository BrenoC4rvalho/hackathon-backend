package com.hackathon.backend.controller;

import com.hackathon.backend.exception.StudentNotFoundException;
import com.hackathon.backend.exception.StudentRegistrationAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentNotFound(StudentNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Student not found", ex.getMessage());
    }

    @ExceptionHandler(StudentRegistrationAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleStudentRegistrationAlreadyExists(
            StudentRegistrationAlreadyExistsException ex
    ) {
        return buildError(HttpStatus.CONFLICT, "Registration number already exists", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildError(
            HttpStatus status,
            String error,
            String message
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
    
}