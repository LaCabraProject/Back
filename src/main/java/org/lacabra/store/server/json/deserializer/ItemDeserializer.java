package org.lacabra.store.server.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;
import org.lacabra.store.server.api.type.user.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;

public final class ItemDeserializer extends JsonDeserializer<Item> {
    private static final ObjectMapper mapper = new ObjectMapperProvider().getContext(Item.class);

    @Override
    public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();

        if (node.isNull())
            return null;

        JsonNode node2;

        ObjectId id = new ObjectMapperProvider().getContext(ObjectId.class).treeToValue(node.get("id"), ObjectId.class);

        ItemType type = null;

        node2 = node.get("type");
        if (!(node2 == null || node2.isNull())) {
            if (!node2.isTextual())
                throw new RuntimeException("\"type\" is not a string.");

            type = ItemType.parse(node2.asText());
            if (type == null)
                throw new RuntimeException(String.format("not a valid item type: %s", node2.asText()));
        }

        String name = null;

        node2 = node.get("name");
        if (!(node2 == null || node2.isNull())) {
            if (!node2.isTextual())
                throw new RuntimeException("\"name\" is not a string.");

            name = node2.asText();
        }

        String description = null;

        var keywords = new HashSet<String>();

        node2 = node.get("keywords");
        if (!(node2 == null || node2.isNull())) {
            if (node2.isTextual())
                keywords.add(node2.asText());

            else {
                if (!node2.isArray())
                    throw new RuntimeException("\"keywords\" is not an array.");

                for (final JsonNode n : node2) {
                    if (!n.isTextual())
                        throw new RuntimeException(String.format ("Keyword is not a string: %s", n.toPrettyString()));

                    keywords.add(n.asText());
                }
            }
        }

        Integer discount = null;
        node2 = node.get()

        BigDecimal price = null;
        BigInteger stock = null;

        UserId parent = new ObjectMapperProvider().getContext(UserId.class).treeToValue(node.get("parent"),
                UserId.class);

        return new Item(id, type, name, description, keywords ,price, discount, stock, new User(parent));
    }
}