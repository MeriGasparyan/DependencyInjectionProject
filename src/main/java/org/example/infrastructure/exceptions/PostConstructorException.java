package org.example.infrastructure.exceptions;

public class PostConstructorException extends RuntimeException {
    public PostConstructorException(String message) {
        super(message);
    }
}
