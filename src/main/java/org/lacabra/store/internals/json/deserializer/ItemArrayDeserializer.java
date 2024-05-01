package org.lacabra.store.internals.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.item.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ItemArrayDeserializer {
    public static final class DTO extends JsonDeserializer<ItemDTO[]> {
        @Override
        public ItemDTO[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            JsonNode node = jp.readValueAsTree();

            if (node.isNull())
                return null;

            final var omp = new ObjectMapperProvider();

            if (!node.isArray())
                node = omp.getContext(ItemDTO[].class).createArrayNode().add(node);

            final List<ItemDTO> items = new ArrayList<>();

            var mapper = omp.getContext(ItemDTO.class);
            for (final JsonNode n : node) {
                items.add(mapper.treeToValue(n, ItemDTO.class));
            }

            return items.toArray(new ItemDTO[0]);
        }
    }

    public static final class Persisent extends JsonDeserializer<Item[]> {
        @Override
        public Item[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            JsonNode node = jp.readValueAsTree();

            if (node.isNull())
                return null;

            final var omp = new ObjectMapperProvider();

            if (!node.isArray())
                node = omp.getContext(Item[].class).createArrayNode().add(node);

            final List<Item> items = new ArrayList<>();

            var mapper = omp.getContext(Item.class);
            for (final JsonNode n : node) {
                items.add(mapper.treeToValue(n, Item.class));
            }

            return items.toArray(new Item[0]);
        }
    }
}