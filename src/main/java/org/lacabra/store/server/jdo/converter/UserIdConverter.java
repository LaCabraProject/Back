package org.lacabra.store.server.jdo.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.lacabra.store.server.api.type.id.UserId;

@Converter(autoApply = true)
public final class UserIdConverter implements AttributeConverter<UserId, String> {
    @Override
    public String convertToDatabaseColumn(UserId id) {
        if (id == null) {
            return null;
        }

        return id.toString();
    }

    @Override
    public UserId convertToEntityAttribute(String id) {
        if (id == null) {
            return null;
        }

        return UserId.from(id);
    }
}