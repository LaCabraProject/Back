package org.lacabra.store.server.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.lacabra.store.server.api.type.user.Authority;

@Converter(autoApply = true)
public final class AuthorityConverter implements AttributeConverter<Authority, String> {
    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if (authority == null) {
            return null;
        }

        return authority.toString();
    }

    @Override
    public Authority convertToEntityAttribute(String authority) {
        if (authority == null) {
            return null;
        }

        return Authority.parse(authority);
    }
}