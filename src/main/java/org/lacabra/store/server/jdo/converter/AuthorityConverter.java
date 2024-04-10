package org.lacabra.store.server.jdo.converter;

import jakarta.persistence.Converter;
import org.lacabra.store.server.api.type.user.Authority;

import javax.jdo.AttributeConverter;

@Converter(autoApply = true)
public final class AuthorityConverter implements AttributeConverter<Authority, String> {
    @Override
    public String convertToDatastore(Authority authority) {
        if (authority == null) {
            return null;
        }

        return authority.toString();
    }

    @Override
    public Authority convertToAttribute(String authority) {
        if (authority == null) {
            return null;
        }

        return Authority.parse(authority);
    }
}