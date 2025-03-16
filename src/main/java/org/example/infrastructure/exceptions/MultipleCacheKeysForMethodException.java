package org.example.infrastructure.exceptions;

public class MultipleCacheKeysForMethodException extends RuntimeException {
    public MultipleCacheKeysForMethodException(String message) {
        super(message);
    }
}
