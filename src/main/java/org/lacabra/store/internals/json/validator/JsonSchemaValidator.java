package org.lacabra.store.internals.json.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.InputFormat;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.OutputFormat;
import com.networknt.schema.ValidationMessage;
import org.lacabra.store.internals.json.exception.JsonSchemaComplianceException;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class JsonSchemaValidator<T> {
    public final static JsonSchemaValidator<User> USER = new JsonSchemaValidator<>(User.class);
    public final static JsonSchemaValidator<UserId> USER_ID = new JsonSchemaValidator<>(UserId.class);
    public final static JsonSchemaValidator<Item> ITEM = new JsonSchemaValidator<>(Item.class);
    public final static JsonSchemaValidator<ObjectId> OBJECT_ID = new JsonSchemaValidator<>(ObjectId.class);

    private static final Map<Class<?>, JsonSchemaValidator<?>> instances = Map.of(User.class, USER, UserId.class,
            USER_ID,
            Item.class, ITEM, ObjectId.class, OBJECT_ID);

    public final JsonSchema schema;

    public JsonSchemaValidator(@NotNull String schema) {
        this(JsonSchemaFactory.getSchema(Objects.requireNonNull(schema)));
    }

    public JsonSchemaValidator(@NotNull Class<T> cls) {
        this(JsonSchemaFactory.getSchema(Objects.requireNonNull(cls)));
    }

    public JsonSchemaValidator(@NotNull JsonSchema schema) {
        this.schema = Objects.requireNonNull(schema);
    }

    public boolean isValid(String json) {
        try {
            this.validate(json);
        } catch (JsonSchemaComplianceException e) {
            return false;
        }

        return true;
    }

    public boolean isValid(JsonNode node) {
        try {
            this.validate(node);
        } catch (JsonSchemaComplianceException e) {
            return false;
        }

        return true;
    }

    public void validate(String json) throws JsonSchemaComplianceException {
        Set<ValidationMessage> asserts = schema.validate(json, InputFormat.JSON, executionContext -> {
            executionContext.getExecutionConfig().setFormatAssertionsEnabled(true);
        });
        Set<String> invalid =
                asserts.stream().filter(x -> !x.isValid()).map(ValidationMessage::getProperty).collect(Collectors.toSet());

        if (!invalid.isEmpty())
            throw new JsonSchemaComplianceException("The following properties are not valid: " + invalid.stream().reduce("",
                    (total, sub) -> total + ", " + sub) + ".");
    }

    public void validate(JsonNode node) throws JsonSchemaComplianceException {
        Set<ValidationMessage> asserts = schema.validate(node, OutputFormat.DEFAULT, executionContext -> {
            executionContext.getExecutionConfig().setFormatAssertionsEnabled(true);
        });

        Set<String> invalid =
                asserts.stream().filter(x -> !x.isValid()).map(ValidationMessage::getProperty).collect(Collectors.toSet());

        if (!invalid.isEmpty())
            throw new JsonSchemaComplianceException("The following properties are not valid: " + invalid.stream().reduce("",
                    (total, sub) -> total + ", " + sub) + ".");
    }
}
