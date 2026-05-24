package com.hackathon.backend.controller;

import com.hackathon.backend.exception.*;
import com.twilio.exception.ApiException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().isEmpty()
                ? "Invalid request"
                : ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return buildError(HttpStatus.BAD_REQUEST, "Validation error", message);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleTwilio(ApiException ex) {
        return buildError(HttpStatus.BAD_GATEWAY, "Twilio error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex.getMessage());
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

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleGroupNotFound(GroupNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Group not found", ex.getMessage());
    }

    @ExceptionHandler(GroupNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleGroupNameAlreadyExists(GroupNameAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, "Group name already exists", ex.getMessage());
    }

    @ExceptionHandler(StudentGroupNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentGroupNotFound(StudentGroupNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Student group not found", ex.getMessage());
    }

    @ExceptionHandler(StudentAlreadyInGroupException.class)
    public ResponseEntity<Map<String, Object>> handleStudentAlreadyInGroup(StudentAlreadyInGroupException ex) {
        return buildError(HttpStatus.CONFLICT, "Student already in group", ex.getMessage());
    }

    @ExceptionHandler(AcademicTaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAcademicTaskNotFound(AcademicTaskNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Academic task not found", ex.getMessage());
    }
}