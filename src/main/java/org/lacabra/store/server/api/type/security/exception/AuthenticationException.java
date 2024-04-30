package org.lacabra.store.server.api.type.security.exception;

import java.io.Serial;

public class AuthenticationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
