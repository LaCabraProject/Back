package org.lacabra.store.server.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.lacabra.store.internals.json.deserializer.ItemDeserializer;
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
        final var jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("id", 0);
        jsonNode.put("type", "clothing");
        jsonNode.put("name", "Camiseta de grupo genérico");
        jsonNode.put("price", 25);
        jsonNode.put("discount", 30);
        jsonNode.put("stock", 1000);
        jsonNode.put("parent", "mikel");

        final var jsonParser = Mockito.mock(JsonParser.class);
        final var deserializationContext = Mockito.mock(DeserializationContext.class);

        when(jsonParser.readValueAsTree()).thenReturn(jsonNode);

        {
            final var item = new ItemDeserializer.Persistent().deserialize(jsonParser, deserializationContext);

            assertNotNull(item);
            assertEquals(item.id().toInteger(), BigInteger.ZERO);
            assertEquals(item.type(), ItemType.Clothing);
            assertEquals(item.name(), "Camiseta de grupo genérico");
            assertEquals(item.price().intValue(), 25);
            assertEquals(item.discount().intValue(), 30);
            assertEquals(item.stock(), BigInteger.valueOf(1000));
            assertEquals(item.parent().id(), "mikel");
        }

        {
            final var item = new ItemDeserializer.DTO().deserialize(jsonParser, deserializationContext);

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
}
