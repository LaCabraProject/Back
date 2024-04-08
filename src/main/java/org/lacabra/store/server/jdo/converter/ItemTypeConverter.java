package org.lacabra.store.server.jdo.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.lacabra.store.server.api.type.item.ItemType;

@Converter(autoApply = true)
public final class ItemTypeConverter implements AttributeConverter<ItemType, String> {
    @Override
    public String convertToDatabaseColumn(ItemType type) {
        if (type == null) {
            return null;
        }

        return type.toString();
    }

    @Override
    public ItemType convertToEntityAttribute(String type) {
        if (type == null) {
            return null;
        }

        return ItemType.parse(type);
    }
}