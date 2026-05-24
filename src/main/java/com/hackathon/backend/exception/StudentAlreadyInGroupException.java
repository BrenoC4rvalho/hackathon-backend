package com.hackathon.backend.exception;

public class StudentAlreadyInGroupException extends RuntimeException {

    public StudentAlreadyInGroupException(Long studentId, Long groupId) {
        super("Student " + studentId + " is already in group " + groupId);
    }
}