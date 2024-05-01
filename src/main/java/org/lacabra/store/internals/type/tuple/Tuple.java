package org.lacabra.store.internals.type.tuple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonSerialize(using = Tuple.Serializer.class)
@JsonDeserialize(using = Tuple.Deserializer.class)
public class Tuple implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Serializable[] members;

    protected Tuple(Serializable... members) {
        this.members = members;
    }

    protected final Serializable get(int idx) {
        if (idx >= this.members.length)
            return null;

        return this.members[idx];
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(Tuple.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class Serializer extends JsonSerializer<Tuple> {
        @Override
        public void serialize(Tuple value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (value == null) {
                jgen.writeNull();

                return;
            }

            var omp = new ObjectMapperProvider();
            var mapper = omp.getContext(Serializable[].class);

            ArrayNode node = mapper.createArrayNode();

            for (Serializable o : value.members) {
                node.add(omp.getContext(o.getClass()).valueToTree(o));
            }

            jgen.writeString(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
        }
    }

    public static final class Deserializer extends JsonDeserializer<Tuple> {
        @Override
        public Tuple deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            JsonNode node = jp.readValueAsTree();

            if (node.isNull())
                return null;

            var omp = new ObjectMapperProvider();

            if (!node.isArray())
                node = omp.getContext(Serializable.class).createArrayNode().add(node);

            return new Tuple(omp.getContext(Serializable[].class).treeToValue(node, Serializable[].class));
        }
    }
}
