package com.hackathon.backend.exception;

public class GroupNameAlreadyExistsException extends RuntimeException {

    public GroupNameAlreadyExistsException(String name) {
        super("Group name already exists: " + name);
    }
}