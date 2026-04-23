package com.project.backend.exception;

public class BotLimitExceededException extends RuntimeException {
    public BotLimitExceededException(String message) {
        super(message);
    }
}