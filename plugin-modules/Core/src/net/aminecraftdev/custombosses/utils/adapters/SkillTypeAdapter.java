package net.aminecraftdev.custombosses.utils.adapters;

import com.google.gson.*;
import net.aminecraftdev.custombosses.skills.enums.SkillType;
import net.aminecraftdev.custombosses.utils.base.BaseAdapter;

import java.lang.reflect.Type;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class SkillTypeAdapter implements BaseAdapter<SkillType> {

    @Override
    public SkillType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String target = jsonElement.getAsString();

        try {
            return SkillType.valueOf(target);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public JsonElement serialize(SkillType skillTarget, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(skillTarget.name().toUpperCase());
    }
}
