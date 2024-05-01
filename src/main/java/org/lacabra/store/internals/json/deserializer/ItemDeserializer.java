package org.lacabra.store.internals.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.dto.UserDTO;
import org.lacabra.store.internals.json.exception.JsonSchemaComplianceException;
import org.lacabra.store.internals.json.validator.JsonSchemaValidator;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;

public final class ItemDeserializer {
    public static final class DTO extends JsonDeserializer<ItemDTO> {
        @Override
        public ItemDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            final var omp = new ObjectMapperProvider();

            final JsonNode node = jp.readValueAsTree();

            try {
                JsonSchemaValidator.ITEM.validate(node);
            } catch (JsonSchemaComplianceException e) {
                Logger.getLogger().warning(e);
                return null;
            }

            JsonNode node2;

            ObjectId id = null;
            node2 = node.get("id");
            if (!(node2 == null || node2.isNull())) {
                id = omp.getContext(ObjectId.class).treeToValue(node2, ObjectId.class);
            }

            ItemType type = null;

            node2 = node.get("type");
            if (!(node2 == null || node2.isNull())) type = ItemType.parse(node2.asText());

            String name = null;

            node2 = node.get("name");
            if (!(node2 == null || node2.isNull())) name = node2.asText();

            String description = null;

            node2 = node.get("description");
            if (!(node2 == null || node2.isNull())) description = node2.asText();

            var keywords = new HashSet<String>();

            node2 = node.get("keywords");
            if (!(node2 == null || node2.isNull())) {
                if (node2.isArray()) for (final JsonNode n : node2) {
                    keywords.add(n.asText());
                }
                else keywords.add(node2.asText());
            }

            Number price = null;
            node2 = node.get("price");
            if (!(node2 == null || node2.isNull())) price = node2.numberValue();

            Integer discount = null;
            node2 = node.get("discount");
            if (!(node2 == null || node2.isNull())) discount = node2.intValue();

            BigInteger stock = null;
            node2 = node.get("stock");
            if (!(node2 == null || node2.isNull())) stock = node2.bigIntegerValue();

            UserId parent = null;
            node2 = node.get("parent");
            if (!(node2 == null || node2.isNull())) {
                parent = omp.getContext(UserId.class).treeToValue(node2, UserId.class);

                if (parent == null) parent = omp.getContext(UserDTO.class).treeToValue(node2, UserDTO.class).id();
            }

            return new ItemDTO(id, type, name, description, keywords, price, discount, stock, parent);
        }
    }

    public static final class Persistent extends JsonDeserializer<Item> {
        @Override
        public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return Item.fromDTO(new ObjectMapperProvider().getContext(ItemDTO.class).treeToValue(jp.readValueAsTree()
                    , ItemDTO.class));
        }
    }
}