package org.lacabra.store.server.api.type.security.exception;

public final class AuthTokenRefreshmentException extends RuntimeException {

    public AuthTokenRefreshmentException(String message) {
        super(message);
    }

    public AuthTokenRefreshmentException(String message, Throwable cause) {
        super(message, cause);
    }
}