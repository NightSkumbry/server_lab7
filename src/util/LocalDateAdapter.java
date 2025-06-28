package util;

import java.lang.reflect.Type;
import java.time.LocalDate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * The LocalDateAdapter class implements {@link JsonSerializer} and {@link JsonDeserializer}
 * to handle the serialization and deserialization of {@link LocalDate} objects with Gson.
 * This adapter allows {@link LocalDate} to be serialized to and deserialized from JSON strings.
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    
    /**
     * Serializes a {@link LocalDate} object into its JSON representation as a string.
     *
     * @param date      The {@link LocalDate} object to be serialized.
     * @param typeOfSrc The actual type of the source object.
     * @param context   The {@link JsonSerializationContext} for custom serialization.
     * @return A {@link JsonElement} representing the serialized date as a string.
     */
    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.toString());
    }

    /**
     * Deserializes a JSON element into a {@link LocalDate} object.
     *
     * @param json     The {@link JsonElement} representing the JSON date string.
     * @param typeOfT  The actual type of the object being deserialized.
     * @param context  The {@link JsonDeserializationContext} for custom deserialization.
     * @return A {@link LocalDate} object parsed from the JSON string.
     * @throws JsonParseException If the JSON element cannot be parsed into a valid {@link LocalDate}.
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString());
    }
}
