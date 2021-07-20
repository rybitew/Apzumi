package com.example.task.exception;

public class InvalidParamException extends RuntimeException {
    public InvalidParamException() {
    }

    public InvalidParamException(String message) {
        super(message);
    }
}
