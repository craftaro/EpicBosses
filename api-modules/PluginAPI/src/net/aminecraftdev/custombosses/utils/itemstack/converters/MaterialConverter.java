package net.aminecraftdev.custombosses.utils.itemstack.converters;

import net.aminecraftdev.custombosses.utils.IConverter;
import net.aminecraftdev.custombosses.utils.NumberUtils;
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
        if(NumberUtils.get().isInt(input)) {
            return Material.getMaterial(Integer.parseInt(input));
        }

        if(input.contains(":")) {
            String[] split = input.split(":");

            input = split[0];

            if(NumberUtils.get().isInt(input)) {
                return Material.getMaterial(Integer.parseInt(input));
            }
        }

        return Material.matchMaterial(input);
    }
}
