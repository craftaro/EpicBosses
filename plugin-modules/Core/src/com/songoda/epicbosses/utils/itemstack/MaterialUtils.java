package com.songoda.epicbosses.utils.itemstack;

import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MaterialUtils {

    private static Map<Integer, Material> materialIdMap;

    static {
        materialIdMap = new HashMap<>();

        for (Material material : Material.values()) {
            try {
                materialIdMap.put(material.getId(), material);
            } catch (Exception ignored) { }
        }
    }

    public static Material fromId(int id) {
        return materialIdMap.get(id);
    }

}
