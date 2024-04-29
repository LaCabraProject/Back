package org.lacabra.store.internals.json.exception;

import java.io.Serial;

public class JsonSchemaComplianceException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public JsonSchemaComplianceException(String message) {
        super(message);
    }

    public JsonSchemaComplianceException(String message, Throwable cause) {
        super(message, cause);
    }
}