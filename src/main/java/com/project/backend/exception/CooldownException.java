package com.project.backend.exception;

public class CooldownException extends RuntimeException {
    public CooldownException(String message) {
        super(message);
    }
}