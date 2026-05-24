package com.hackathon.backend.exception;

public class AcademicTaskNotFoundException extends RuntimeException {

    public AcademicTaskNotFoundException(Long id) {
        super("Academic task not found with id: " + id);
    }
}
