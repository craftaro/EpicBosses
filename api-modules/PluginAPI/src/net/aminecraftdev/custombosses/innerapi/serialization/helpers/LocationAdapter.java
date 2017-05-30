package net.aminecraftdev.custombosses.innerapi.serialization.helpers;

import com.google.gson.*;
import net.aminecraftdev.custombosses.innerapi.serialization.Serialize;
import org.bukkit.Location;

import java.lang.reflect.Type;

/**
 * @author Debugged
 * @version 1.0
 * @since 18-5-2017
 */
public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Serialize.deserializeLocation(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(
                Serialize.serialize(location)
        );
    }

}
