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

import javax.validation.constraints.Null;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;

public final class ItemDeserializer extends JsonDeserializer<Item> {
    private static final ObjectMapper mapper = new ObjectMapperProvider().getContext(Item.class);

    @Override
    public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}