package net.aminecraftdev.custombosses.utils.itemstack;

import net.aminecraftdev.custombosses.utils.IConverter;
import net.aminecraftdev.custombosses.utils.exceptions.NotImplementedException;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
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
        if(configurationSection == null) return null;

        Integer amount = (Integer) configurationSection.get("amount", null);
        String type = configurationSection.getString("type", null);
        Short durability = (Short) configurationSection.get("durability", null);
        String name = configurationSection.getString("name", null);
        List<String> lore = (List<String>) configurationSection.getList("lore", null);
        List<String> enchants = (List<String>) configurationSection.getList("enchants", null);
        String skullOwner = configurationSection.getString("skullOwner", null);
        Short spawnerId = (Short) configurationSection.get("spawnerId", null);
        Boolean isGlowing = (Boolean) configurationSection.get("isGlowing", null);

        return new ItemStackHolder(amount, type, durability, name, lore, enchants, skullOwner, spawnerId, isGlowing);
    }

    @Override
    public ConfigurationSection from(ItemStackHolder itemStackHolder) throws NotImplementedException {
        throw new NotImplementedException("An ItemStackHolder cannot be converted to a ConfigurationSection.");
    }
}
