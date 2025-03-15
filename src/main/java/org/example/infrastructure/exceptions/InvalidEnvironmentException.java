package org.example.infrastructure.exceptions;

public class InvalidEnvironmentException extends RuntimeException {
    public InvalidEnvironmentException(String message) {
        super(message);
    }
}
