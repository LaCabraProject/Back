package org.lacabra.store.server.jdo.converter;


import jakarta.persistence.Converter;
import org.lacabra.store.internals.type.id.UserId;

import javax.jdo.AttributeConverter;

@Converter(autoApply = true)
public final class UserIdConverter implements AttributeConverter<UserId, String> {
    @Override
    public String convertToDatastore(UserId id) {
        if (id == null) {
            return null;
        }

        return id.toString();
    }

    @Override
    public UserId convertToAttribute(String id) {
        if (id == null) {
            return null;
        }

        return UserId.from(id);
    }
}