package net.aminecraftdev.custombosses.utils.itemstack;

import net.aminecraftdev.custombosses.utils.EnchantFinder;
import net.aminecraftdev.custombosses.utils.IConverter;
import net.aminecraftdev.custombosses.utils.StringUtils;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class ItemStackConverter implements IConverter<ItemStackHolder, ItemStack> {

    private static final StringUtils STRING_UTILS = new StringUtils();

    @Override
    public ItemStackHolder to(ItemStack itemStack) {
        Material material = itemStack.getType();
        Short durability = itemStack.getDurability();
        String type = null, name = null, skullOwner = null, spawnerType = null;
        List<String> lore = null, enchants = null;

        if(durability == 0) {
            durability = null;
        }

        if(itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if(itemMeta.hasDisplayName()) {
                name = STRING_UTILS.stripColor(itemMeta.getDisplayName());
            }

            if(itemMeta.hasLore()) {
                lore = new ArrayList<>();

                for(String string : itemMeta.getLore()) {
                    lore.add(STRING_UTILS.stripColor(string));
                }
            }

            if(itemMeta.hasEnchants()) {
                enchants = new ArrayList<>();

                for(Map.Entry<Enchantment, Integer> entry : itemMeta.getEnchants().entrySet()) {
                    int level = entry.getValue();
                    Enchantment enchantment = entry.getKey();
                    EnchantFinder enchantFinder = EnchantFinder.getByEnchant(enchantment);

                    if(enchantFinder == null) {
                        throw new EnumConstantNotPresentException(EnchantFinder.class, "EnchantFinder couldn't find a value for " + enchantment.getName() + ". Please report this to @AMinecraftDev so he can fix it.");
                    }

                    enchants.add(enchantFinder.getFancyName() + ":" + level);
                }
            }

            if(itemMeta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;

                if(skullMeta.hasOwner()) {
                    skullOwner = skullMeta.getOwner();
                }
            }
        }

        return new ItemStackHolder(type, durability, name, lore, enchants, skullOwner, spawnerType, null);
    }

    @Override
    public ItemStack from(ItemStackHolder itemStackHolder) {
        return null;
    }
}
