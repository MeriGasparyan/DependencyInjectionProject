package org.example.infrastructure.exceptions;

public class NoCacheKeyException extends RuntimeException {
    public NoCacheKeyException(String message) {
        super(message);
    }
}
