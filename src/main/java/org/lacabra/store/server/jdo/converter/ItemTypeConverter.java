package org.lacabra.store.server.jdo.converter;

import jakarta.persistence.Converter;
import org.lacabra.store.server.api.type.item.ItemType;

import javax.jdo.AttributeConverter;

@Converter(autoApply = true)
public final class ItemTypeConverter implements AttributeConverter<ItemType, String> {
    @Override
    public String convertToDatastore(ItemType type) {
        if (type == null) {
            return null;
        }

        return type.toString();
    }

    @Override
    public ItemType convertToAttribute(String type) {
        if (type == null) {
            return null;
        }

        return ItemType.parse(type);
    }
}