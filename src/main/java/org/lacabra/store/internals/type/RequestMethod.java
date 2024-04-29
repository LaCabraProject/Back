package org.lacabra.store.internals.type;

import java.io.Serializable;

public enum RequestMethod implements Serializable {
    GET(Constants.GET),
    HEAD(RequestMethod.Constants.HEAD),
    POST(RequestMethod.Constants.POST),
    PUT(RequestMethod.Constants.PUT),
    DELETE(RequestMethod.Constants.DELETE),
    CONNECT(RequestMethod.Constants.CONNECT),
    OPTIONS(RequestMethod.Constants.OPTIONS),
    TRACE(RequestMethod.Constants.TRACE),
    PATCH(RequestMethod.Constants.PATCH);

    private final String method;

    RequestMethod(final String method) {
        this.method = method;
    }

    public static RequestMethod parse(final String method) {
        return switch (method) {
            case RequestMethod.Constants.GET -> GET;
            case RequestMethod.Constants.HEAD -> HEAD;
            case RequestMethod.Constants.POST -> POST;
            case RequestMethod.Constants.PUT -> PUT;
            case RequestMethod.Constants.DELETE -> DELETE;
            case RequestMethod.Constants.CONNECT -> CONNECT;
            case RequestMethod.Constants.OPTIONS -> OPTIONS;
            case RequestMethod.Constants.TRACE -> TRACE;
            case RequestMethod.Constants.PATCH -> PATCH;

            case null, default -> null;
        };
    }

    @Override
    public String toString() {
        return this.method;
    }

    private static final class Constants {
        public static final String GET = "GET";
        public static final String HEAD = "HEAD";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
        public static final String CONNECT = "CONNECT";
        public static final String OPTIONS = "OPTIONS";
        public static final String TRACE = "TRACE";
        public static final String PATCH = "PATCH";
    }
}