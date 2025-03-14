package org.example.infrastructure.exceptions;

public class NotFrameworkHandledClassException extends RuntimeException {
    public NotFrameworkHandledClassException(String message) {
        super(message);
    }
}
