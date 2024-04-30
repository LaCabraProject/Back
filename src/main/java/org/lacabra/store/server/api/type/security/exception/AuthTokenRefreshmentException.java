package org.lacabra.store.server.api.type.security.exception;

import java.io.Serial;

public class AuthTokenRefreshmentException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthTokenRefreshmentException(String message) {
        super(message);
    }

    public AuthTokenRefreshmentException(String message, Throwable cause) {
        super(message, cause);
    }
}