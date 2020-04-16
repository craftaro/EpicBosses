package com.songoda.epicbosses.utils.itemstack;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.epicbosses.utils.IConverter;
import com.songoda.epicbosses.utils.exceptions.NotImplementedException;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Jul-18
 */
@SuppressWarnings("unchecked")
public class ItemStackHolderConverter implements IConverter<ItemStackHolder, ConfigurationSection> {

    @Override
    public ItemStackHolder to(ConfigurationSection configurationSection) {
        if (configurationSection == null) return null;

        Integer amount = (Integer) configurationSection.get("amount", null);
        CompatibleMaterial material = CompatibleMaterial.getMaterial(configurationSection.getString("type", null));

        String type;
        if (material == null)
            type = "STONE";
        else
            type = material.getMaterial() == null ? "STONE" : material.getMaterial().name();

        Short durability = (Short) configurationSection.get("durability", null);
        if (material.getData() != -1) durability = (short) material.getData();

        String name = configurationSection.getString("name", null);
        List<String> lore = (List<String>) configurationSection.getList("lore", null);
        List<String> enchants = (List<String>) configurationSection.getList("enchants", null);
        String skullOwner = configurationSection.getString("skullOwner", null);
        Short spawnerId = (Short) configurationSection.get("spawnerId", null);
        //Boolean isGlowing = (Boolean) configurationSection.get("isGlowing", null);

        return new ItemStackHolder(amount, type, durability, name, lore, enchants, skullOwner, spawnerId);
    }

    @Override
    public ConfigurationSection from(ItemStackHolder itemStackHolder) throws NotImplementedException {
        throw new NotImplementedException("An ItemStackHolder cannot be converted to a ConfigurationSection.");
    }
}
