package com.songoda.epicbosses.utils.itemstack;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.epicbosses.utils.IConverter;
import com.songoda.epicbosses.utils.exceptions.NotImplementedException;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolderJson;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolderLegacy;
import java.util.Arrays;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import org.bukkit.entity.EntityType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Jul-18
 */
@SuppressWarnings("unchecked")
public class ItemStackHolderConverter implements IConverter<ItemStackHolder, ConfigurationSection> {

    @Override
    public ItemStackHolder to(ConfigurationSection configurationSection) {
        return ItemStackUtils.getItemStackHolder(configurationSection);
    }

    @Override
    public ConfigurationSection from(ItemStackHolder itemStackHolder) throws NotImplementedException {
        throw new NotImplementedException("An ItemStackHolder cannot be converted to a ConfigurationSection.");
    }
}
