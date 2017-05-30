package net.aminecraftdev.custombosses.innerapi.itemstack;

import net.aminecraftdev.custombosses.innerapi.factory.CloneableFactory;
import net.aminecraftdev.custombosses.innerapi.factory.Factory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by charl on 04-May-17.
 */
public class ItemFactory extends Factory<ItemStack> implements CloneableFactory<ItemFactory> {

    private ItemMeta itemMeta;

    public ItemFactory(Material material, byte data) {
        this.object = new ItemStack(material, 1, (short) data);
        this.itemMeta = this.object.getItemMeta();
    }

    public ItemFactory(Material material) {
        this(material, (byte) 0);
    }

    public final ItemFactory setAmount(int amount) {
        if(amount > 64) amount = 64;

        this.object.setAmount(amount);
        return this;
    }

    public final ItemFactory setName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public final ItemFactory setLore(List<String> lore) {
        this.itemMeta.setLore(lore);
        return this;
    }

    public final ItemFactory setLore(String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public final ItemFactory setOwner(String name) {
        if(this.object.getType() == Material.SKULL_ITEM) {
            SkullMeta skullMeta = (SkullMeta) this.itemMeta;

            skullMeta.setOwner(name);
        }

        return this;
    }

    public final ItemFactory setDurability(short durability) {
        this.object.setDurability(durability);
        return this;
    }

    public final ItemFactory setData(byte data) {
        this.object.setDurability((short) data);
        return this;
    }

    public final ItemFactory setUnsafeEnchantment(Enchantment enchantment, int level) {
        this.object.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public final ItemFactory setEnchantment(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    @Override
    public final ItemStack build() {
        this.object.setItemMeta(this.itemMeta);
        return this.object;
    }

    @Override
    public final ItemFactory clone() {
        ItemFactory clone = new ItemFactory(this.object.getType(), this.object.getData().getData());
        clone.itemMeta = this.itemMeta;
        return clone;
    }
}
