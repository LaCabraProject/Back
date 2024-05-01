package org.lacabra.store.server.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.lacabra.store.internals.json.deserializer.ItemDeserializer;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class ItemDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("id", 0);
        jsonNode.put("type", "clothing");
        jsonNode.put("name", "Camiseta de grupo genérico");
        jsonNode.put("price", 25);
        jsonNode.put("discount", 30);
        jsonNode.put("stock", 1000);
        jsonNode.put("parent", "mikel");

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        when(jsonParser.readValueAsTree()).thenReturn(jsonNode);

        JsonDeserializer<Item> itemDeserializer = new ItemDeserializer.Persistent();

        Item item = itemDeserializer.deserialize(jsonParser, deserializationContext);

        assertNotNull(item);
        assertEquals(item.id().toInteger(), BigInteger.ZERO);
        assertEquals(item.type(), ItemType.Clothing);
        assertEquals(item.name(), "Camiseta de grupo genérico");
        assertEquals(item.price().intValue(), 25);
        assertEquals(item.discount().intValue(), 30);
        assertEquals(item.stock(), BigInteger.valueOf(1000));
        assertEquals(item.parent(), "mikel");
    }
}
