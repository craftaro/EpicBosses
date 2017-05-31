package net.aminecraftdev.custombosses.handlers.builders;

import net.aminecraftdev.custombosses.innerapi.itemstack.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class EquipmentHandler {

    public void applyEquipment(LivingEntity livingEntity, ConfigurationSection configurationSection) {
        Map<Enchantment, Integer> mapOfEnchants = getMapOfEnchants(configurationSection);
        String type = configurationSection.getString("type").toUpperCase();

        if(configurationSection.getName().equalsIgnoreCase("Armor")) {
            String innerType = type + "_HELMET";

            livingEntity.getEquipment().setHelmet(getArmour(innerType, mapOfEnchants));
            innerType = type + "_CHESTPLATE";
            livingEntity.getEquipment().setChestplate(getArmour(innerType, mapOfEnchants));
            innerType = type + "_LEGGINGS";
            livingEntity.getEquipment().setLeggings(getArmour(innerType, mapOfEnchants));
            innerType = type + "_BOOTS";
            livingEntity.getEquipment().setBoots(getArmour(innerType, mapOfEnchants));
        } else {
            ItemStack itemStack = new ItemStack(ItemStackUtils.getType(type));

            itemStack.addUnsafeEnchantments(mapOfEnchants);
            livingEntity.getEquipment().setItemInHand(itemStack);
        }
    }

    public void applySkull(LivingEntity livingEntity, ConfigurationSection configurationSection) {
        String owner = configurationSection.getString("owner");
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setOwner(owner);
        itemStack.setItemMeta(skullMeta);
        livingEntity.getEquipment().setHelmet(itemStack);
    }

    private Map<Enchantment, Integer> getMapOfEnchants(ConfigurationSection configurationSection) {
        Map<Enchantment, Integer> enchantmentIntegerMap = new HashMap<>();
        List<String> enchantsList = configurationSection.getStringList("enchants");

        for(String s : enchantsList) {
            String[] spl = s.split(":");
            String ench = spl[0];
            int level = Integer.parseInt(spl[1]);

            enchantmentIntegerMap.put(Enchantment.getByName(ench), level);
        }

        return enchantmentIntegerMap;
    }

    private ItemStack getArmour(String type, Map<Enchantment, Integer> enchantments) {
        ItemStack itemStack = new ItemStack(ItemStackUtils.getType(type));

        itemStack.addUnsafeEnchantments(enchantments);

        return itemStack;
    }

}
