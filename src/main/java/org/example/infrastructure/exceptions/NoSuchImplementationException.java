package org.example.infrastructure.exceptions;

public class NoSuchImplementationException extends RuntimeException {
    public NoSuchImplementationException(String message) {
        super(message);
    }
}
