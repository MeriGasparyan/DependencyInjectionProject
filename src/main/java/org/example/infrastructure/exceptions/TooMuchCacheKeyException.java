package org.example.infrastructure.exceptions;

public class TooMuchCacheKeyException extends RuntimeException {
    public TooMuchCacheKeyException(String message) {
        super(message);
    }
}
