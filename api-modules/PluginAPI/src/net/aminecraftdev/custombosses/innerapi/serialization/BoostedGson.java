package net.aminecraftdev.custombosses.innerapi.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.aminecraftdev.custombosses.innerapi.serialization.helpers.LocationAdapter;
import org.bukkit.Location;

/**
 * @author Debugged
 * @version 1.0
 * @since 18-5-2017
 */
public class BoostedGson {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .create();

    private static final Gson gsonFlat = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .create();

    public static Gson getGson() {
        return gson;
    }

    public static Gson getGsonFlat() {
        return gsonFlat;
    }

}
