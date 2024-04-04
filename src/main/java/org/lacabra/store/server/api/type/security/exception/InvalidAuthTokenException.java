package org.lacabra.store.server.api.type.security.exception;

public final class InvalidAuthTokenException extends RuntimeException {

    public InvalidAuthTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
