package net.aminecraftdev.custombosses.utils.adapters;

import com.google.gson.*;
import net.aminecraftdev.custombosses.skills.enums.SkillTarget;
import net.aminecraftdev.custombosses.utils.base.BaseAdapter;

import java.lang.reflect.Type;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class SkillTargetAdapter implements BaseAdapter<SkillTarget> {

    @Override
    public SkillTarget deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String target = jsonElement.getAsString();

        try {
            return SkillTarget.valueOf(target);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public JsonElement serialize(SkillTarget skillTarget, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(skillTarget.name().toUpperCase());
    }

}
