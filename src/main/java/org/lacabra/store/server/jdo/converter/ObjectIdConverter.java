package org.lacabra.store.server.jdo.converter;

import jakarta.persistence.Converter;
import org.lacabra.store.internals.type.id.ObjectId;

import javax.jdo.AttributeConverter;

@Converter(autoApply = true)
public final class ObjectIdConverter implements AttributeConverter<ObjectId, String> {
    @Override
    public String convertToDatastore(ObjectId id) {
        return ObjectId.normalize(id);
    }

    @Override
    public ObjectId convertToAttribute(String id) {
        return ObjectId.from(id);
    }
}