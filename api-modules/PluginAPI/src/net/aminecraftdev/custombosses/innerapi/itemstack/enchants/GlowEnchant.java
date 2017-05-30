package net.aminecraftdev.custombosses.innerapi.itemstack.enchants;

import net.aminecraftdev.custombosses.innerapi.factory.NbtFactory;
import org.bukkit.inventory.ItemStack;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 29-May-17
 */
public class GlowEnchant {

    public static final ItemStack addGlow(ItemStack base) {
        ItemStack craftStack = NbtFactory.getCraftItemStack(base);
        NbtFactory.NbtCompound compound = NbtFactory.fromItemTag(craftStack);
        NbtFactory.NbtList newList = NbtFactory.createList();

        compound.put("ench", newList);
        return craftStack;
    }

}
