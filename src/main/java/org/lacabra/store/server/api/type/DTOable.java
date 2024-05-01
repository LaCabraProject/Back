package org.lacabra.store.server.api.type;

public interface DTOable<Persistent, DTO extends Record> {
    DTO toDTO();
}
