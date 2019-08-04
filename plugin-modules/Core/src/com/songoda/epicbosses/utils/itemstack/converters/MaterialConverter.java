package com.songoda.epicbosses.utils.itemstack.converters;

import com.songoda.epicbosses.utils.IConverter;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.itemstack.MaterialUtils;
import org.bukkit.Material;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class MaterialConverter implements IConverter<String, Material> {

    @Override
    public String to(Material material) {
        return material.name().toLowerCase();
    }

    @Override
    public Material from(String input) {
        if(input.contains(":")) {
            String[] split = input.split(":");

            input = split[0];
        }

        if(NumberUtils.get().isInt(input)) {
            return MaterialUtils.fromId(NumberUtils.get().getInteger(input));
        }

        return Material.matchMaterial(input);
    }
}
