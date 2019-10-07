package com.songoda.epicbosses.utils.adapters;

import com.google.gson.*;
import com.songoda.epicbosses.utils.base.BaseAdapter;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class PotionEffectTypeAdapter implements BaseAdapter<PotionEffectType> {

    @Override
    public PotionEffectType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String effectType = jsonElement.getAsString();
        PotionEffectType potionEffectType = PotionEffectType.getByName(effectType.toUpperCase());

        if (potionEffectType == null) return null;

        return potionEffectType;
    }

    @Override
    public JsonElement serialize(PotionEffectType potionEffectType, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(potionEffectType.getName().toUpperCase());
    }
}
