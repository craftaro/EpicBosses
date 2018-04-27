package net.aminecraftdev.custombosses.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.aminecraftdev.custombosses.utils.adapters.PotionEffectTypeAdapter;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class BossesGson {

    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(PotionEffectType.class, new PotionEffectTypeAdapter())
            .create();

    public static Gson get() {
        return gson;
    }

}
