package org.lacabra.store.server.jdo.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.lacabra.store.server.api.type.id.ObjectId;

@Converter(autoApply = true)
public final class ObjectIdConverter implements AttributeConverter<ObjectId, String> {
    @Override
    public String convertToDatabaseColumn(ObjectId id) {
        if (id == null) {
            return null;
        }

        return id.toString();
    }

    @Override
    public ObjectId convertToEntityAttribute(String id) {
        if (id == null) {
            return null;
        }

        return ObjectId.from(id);
    }
}