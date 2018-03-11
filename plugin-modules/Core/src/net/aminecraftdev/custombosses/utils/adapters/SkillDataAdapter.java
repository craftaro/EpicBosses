package net.aminecraftdev.custombosses.utils.adapters;

import com.google.gson.*;
import net.aminecraftdev.custombosses.utils.base.BaseSkillData;
import net.aminecraftdev.custombosses.utils.base.BaseAdapter;

import java.lang.reflect.Type;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class SkillDataAdapter implements BaseAdapter<BaseSkillData> {

    @Override
    public BaseSkillData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject member = (JsonObject) jsonElement;
        final JsonElement typeString = get(member, "type");
        final JsonElement data = get(member, "data");
        final Type actualType = typeForName(typeString);

        return jsonDeserializationContext.deserialize(data, actualType);
    }

    @Override
    public JsonElement serialize(BaseSkillData baseSkillData, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", baseSkillData.getClass().getName());
        jsonObject.add("data", jsonSerializationContext.serialize(baseSkillData));

        return jsonObject;
    }

    private Type typeForName(final JsonElement typeElem) {
        try {
            return Class.forName(typeElem.getAsString());
        }
        catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    private JsonElement get(final JsonObject wrapper, final String memberName) {
        final JsonElement elem = wrapper.get(memberName);

        if (elem == null) {
            throw new JsonParseException(
                    "no '" + memberName + "' member found in json file.");
        }
        return elem;
    }
}
