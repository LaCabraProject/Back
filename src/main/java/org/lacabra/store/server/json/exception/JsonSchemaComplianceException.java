package org.lacabra.store.server.json.exception;

public class JsonSchemaComplianceException extends Exception {

    public JsonSchemaComplianceException(String message) {
        super(message);
    }

    public JsonSchemaComplianceException(String message, Throwable cause) {
        super(message, cause);
    }
}