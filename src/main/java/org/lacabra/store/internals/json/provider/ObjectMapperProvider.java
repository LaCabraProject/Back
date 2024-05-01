package org.lacabra.store.internals.json.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.dto.UserDTO;
import org.lacabra.store.internals.json.deserializer.*;
import org.lacabra.store.internals.json.serializer.BigDecimalSerializer;
import org.lacabra.store.internals.json.serializer.BigIntegerSerializer;
import org.lacabra.store.internals.json.serializer.ObjectIdSerializer;
import org.lacabra.store.internals.json.serializer.UserIdSerializer;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import javax.validation.constraints.NotNull;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private final Map<Class<?>, ObjectMapper> mappers = new HashMap<>();

    public ObjectMapperProvider() {
        super();
    }

    private static ObjectMapper createObjectMapper() {
        final var mapper = new ObjectMapper();

        final var mod = new SimpleModule();

        mod.addDeserializer(User[].class, new UserArrayDeserializer.Persisent());
        mod.addDeserializer(UserDTO[].class, new UserArrayDeserializer.DTO());

        mod.addDeserializer(User.class, new UserDeserializer.Persistent());
        mod.addDeserializer(UserDTO.class, new UserDeserializer.DTO());

        mod.addDeserializer(Item[].class, new ItemArrayDeserializer.Persisent());
        mod.addDeserializer(ItemDTO[].class, new ItemArrayDeserializer.DTO());

        mod.addDeserializer(Item.class, new ItemDeserializer.Persistent());
        mod.addDeserializer(ItemDTO.class, new ItemDeserializer.DTO());

        mod.addDeserializer(UserId.class, new UserIdDeserializer());
        mod.addDeserializer(ObjectId.class, new ObjectIdDeserializer());

        mod.addSerializer(UserId.class, new UserIdSerializer());
        mod.addSerializer(ObjectId.class, new ObjectIdSerializer());

        mod.addSerializer(BigInteger.class, new BigIntegerSerializer());
        mod.addSerializer(BigDecimal.class, new BigDecimalSerializer());

        mapper.registerModule(mod);
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new ParameterNamesModule());

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper;
    }

    @Override
    public ObjectMapper getContext(@NotNull final Class<?> type) {
        if (type == null)
            return null;

        if (mappers.containsKey(type))
            return mappers.get(type);

        final ObjectMapper m = createObjectMapper();
        mappers.put(type, m);

        return m;
    }
}