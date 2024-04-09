package org.lacabra.store.server.jdo.converter;

import jakarta.persistence.Converter;
import org.lacabra.store.server.api.type.id.ObjectId;

import javax.jdo.AttributeConverter;

@Converter(autoApply = true)
public final class ObjectIdConverter implements AttributeConverter<ObjectId, String> {
    @Override
    public String convertToDatastore(ObjectId id) {
        if (id == null) {
            return null;
        }

        return id.toString();
    }

    @Override
    public ObjectId convertToAttribute(String id) {
        if (id == null) {
            return null;
        }

        return ObjectId.from(id);
    }
}