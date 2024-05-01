package org.lacabra.store.client.dto;

import java.io.Serializable;

public interface DTO<DTO extends Record & Serializable, Persistent> {
    Persistent toPersistent();
}
