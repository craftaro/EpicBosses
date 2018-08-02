package net.aminecraftdev.custombosses.utils.itemstack.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 08-Jun-17
 */
public class GlowEnchant {

    public static ItemStack addGlow(ItemStack base) {
        base.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        ItemMeta itemMeta = base.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        base.setItemMeta(itemMeta);

        return base;
    }

}
