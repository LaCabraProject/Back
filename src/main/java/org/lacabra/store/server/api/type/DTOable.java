package org.lacabra.store.server.api.type;

import java.io.Serializable;

public interface DTOable<Persistent, DTO extends Record & Serializable> {
    DTO toDTO();
}
