package org.lacabra.store.server.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.json.exception.JsonSchemaComplianceException;
import org.lacabra.store.server.json.validator.JsonSchemaFactory;
import org.lacabra.store.server.json.validator.JsonSchemaValidator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;

public final class ItemDeserializer extends JsonDeserializer<Item> {
    private static final ObjectMapperProvider omp = new ObjectMapperProvider();
    private static final ObjectMapper mapper = omp.getContext(Item.class);
    private static final JsonSchema schema = JsonSchemaFactory.getSchema("item");

    @Override
    public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();

        try {
            JsonSchemaValidator.ITEM.validate(node);
        } catch (JsonSchemaComplianceException e) {
            Logger.getLogger().warning(e);
            return null;
        }

        JsonNode node2;

        ObjectId id = omp.getContext(ObjectId.class).treeToValue(node.get("id"), ObjectId.class);
        ItemType type = ItemType.parse(node.get("type").asText());

        String name = node.get("name").asText();
        String description = null;

        node2 = node.get("description");
        if (!(node2 == null || node2.isNull()))
            description = node2.asText();

        var keywords = new HashSet<String>();

        node2 = node.get("keywords");
        if (!(node2 == null || node2.isNull())) {
            if (node2.isArray())
                for (final JsonNode n : node2) {
                    keywords.add(n.asText());
                }
            else
                keywords.add(node2.asText());
        }

        Number price = null;
        node2 = node.get("price");
        if (!(node2 == null || node2.isNull()))
            price = node2.numberValue();

        int discount = node.get("price").asInt(0);

        BigInteger stock = null;
        node2 = node.get("stock");
        if (!(node2 == null || node2.isNull()))
            stock = node2.bigIntegerValue();

        UserId parent = omp.getContext(UserId.class).treeToValue(node.get("parent"), UserId.class);

        return new Item(id, type, name, description, keywords, price, discount, stock, new User(parent));
    }
}