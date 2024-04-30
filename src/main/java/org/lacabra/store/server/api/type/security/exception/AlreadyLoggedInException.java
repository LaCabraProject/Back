package org.lacabra.store.server.api.type.security.exception;

import java.io.Serial;

public class AlreadyLoggedInException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyLoggedInException(String message) {
        super(message);
    }

    public AlreadyLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}

