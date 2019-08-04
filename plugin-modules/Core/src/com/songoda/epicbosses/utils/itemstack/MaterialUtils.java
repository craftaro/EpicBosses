package com.songoda.epicbosses.utils.itemstack;

import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MaterialUtils {

    private static final VersionHandler versionHandler = new VersionHandler();
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

    public static Material getSkullMaterial() {
        if (versionHandler.getVersion().isHigherThanOrEqualTo(Versions.v1_13_R1)) {
            return Material.PLAYER_HEAD;
        } else {
            return Material.getMaterial("SKULL_ITEM");
        }
    }

}
