package org.lacabra.store.server.api.type.security.exception;

import java.io.Serial;

public class InvalidAuthTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidAuthTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
