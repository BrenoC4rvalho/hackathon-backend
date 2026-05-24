package com.hackathon.backend.exception;

public class StudentGroupNotFoundException extends RuntimeException {

    public StudentGroupNotFoundException(Long id) {
        super("Student group not found with id: " + id);
    }
}