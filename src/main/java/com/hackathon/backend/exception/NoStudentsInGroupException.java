package com.hackathon.backend.exception;

public class NoStudentsInGroupException extends RuntimeException {

    public NoStudentsInGroupException(Long groupId) {
        super("No students linked to group with id: " + groupId);
    }
}
