package org.lacabra.store.server.api.type.security.exception;

public class AlreadyLoggedInException extends RuntimeException {

    public AlreadyLoggedInException(String message) {
        super(message);
    }

    public AlreadyLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}

