package org.lacabra.store.server.api.type.user;

import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
public class UserData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}