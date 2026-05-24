package com.hackathon.backend.exception;

public class StudentRegistrationAlreadyExistsException extends RuntimeException {

    public StudentRegistrationAlreadyExistsException(String registrationNumber) {
        super("Registration number already exists: " + registrationNumber);
    }
}