package net.aminecraftdev.custombosses.innerapi.itemstack;

import net.aminecraftdev.custombosses.innerapi.NumberUtils;
import net.aminecraftdev.custombosses.innerapi.factory.NbtFactory;
import net.aminecraftdev.custombosses.innerapi.itemstack.enchants.GlowEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by charl on 28-Apr-17.
 */
public class ItemStackUtils {

    public static final ItemStack createItemStack(ConfigurationSection configurationSection) {
        return createItemStack(configurationSection, 1, null);
    }

    public static final ItemStack createItemStack(ConfigurationSection configurationSection, int amount, Map<String, String> replacedMap) {
        String type = configurationSection.getString("type");
        String name = configurationSection.getString("name");
        List<String> lore = configurationSection.getStringList("lore");
        List<String> enchants = configurationSection.getStringList("enchants");
        short durability = (short) configurationSection.getInt("durability");
        Map<Enchantment, Integer> map = new HashMap<>();
        List<String> newLore = new ArrayList<>();
        Material mat = getType(type);
        short meta = 0;
        boolean addGlow = false;

        if(type instanceof String) {
            String sType = (String) type;

            if(sType.contains(":")) {
                String[] split = sType.split(":");

                meta = Short.valueOf(split[1]);
            }
        }

        if((replacedMap != null) && (name != null)) {
            for(String z : replacedMap.keySet()) {
                if(!name.contains(z)) continue;

                name = name.replace(z, replacedMap.get(z));
            }
        }

        if(lore != null) {
            for(String z : lore) {
                String y = z;

                if(replacedMap != null) {
                    for(String x : replacedMap.keySet()) {
                        if(!y.contains(x)) continue;

                        y = y.replace(x, replacedMap.get(x));
                    }
                }

                if(y.contains("\n")) {
                    String[] split = y.split("\n");

                    for(String s2 : split) {
                        newLore.add(ChatColor.translateAlternateColorCodes('&', s2));
                    }
                } else {
                    newLore.add(ChatColor.translateAlternateColorCodes('&', y));
                }
            }
        }

        if(enchants != null) {
            for(String s : enchants) {
                String[] spl = s.split(":");
                String ench = spl[0];
                int level = Integer.parseInt(spl[1]);

                if(ench.equalsIgnoreCase("glow")) {
                    addGlow = true;
                    continue;
                }

                map.put(Enchantment.getByName(ench), level);
            }
        }

        ItemStack itemStack = new ItemStack(mat, amount, meta);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(!newLore.isEmpty()) {
            itemMeta.setLore(newLore);
        }
        if(name != null) {
            if(!name.equals("")) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }
        }

        itemStack.setItemMeta(itemMeta);

        if(!map.isEmpty()) {
            itemStack.addUnsafeEnchantments(map);
        }
        if(configurationSection.contains("durability")) {
            short dura = itemStack.getType().getMaxDurability();
            dura -= (short) durability - 1;

            itemStack.setDurability(dura);
        }

        if(addGlow) return addGlow(itemStack);

        return itemStack;
    }

    public static final ItemStack replicateItem(ItemStack replicatable, int amount, Map<String, String> replaceableData, Map<String, Object> compoundData) {
        ItemStack clone = replicatable.clone();
        ItemMeta itemMeta = clone.getItemMeta();
        String name = itemMeta.hasDisplayName()? itemMeta.getDisplayName() : "";
        List<String> lore = itemMeta.hasLore()? itemMeta.getLore() : new ArrayList<>();
        List<String> newLore = new ArrayList<>();

        if(replaceableData != null) {
            for(String line : lore) {
                for(String s : replaceableData.keySet()) {
                    if(line.contains(s)) line = line.replace(s, replaceableData.get(s));
                }

                newLore.add(line);
            }

            for(String s : replaceableData.keySet()) {
                if(name.contains(s)) name = name.replace(s, replaceableData.get(s));
            }
        } else {
            newLore.addAll(lore);
        }

        itemMeta.setLore(newLore);
        itemMeta.setDisplayName(name);
        clone.setItemMeta(itemMeta);
        clone.setAmount(amount);

        if(compoundData == null) return clone;

        ItemStack craftStack = NbtFactory.getCraftItemStack(clone);
        NbtFactory.NbtCompound compound = NbtFactory.fromItemTag(craftStack);

        for(String s : compoundData.keySet()) {
            compound.put(s, compoundData.get(s));
        }

        return craftStack;
    }

    public static final Material getType(String string) {
        Material material = Material.getMaterial(string);

        if(material == null) {
            if(NumberUtils.isStringInteger(string)) {
                material = Material.getMaterial(Integer.valueOf(string));

                if(material != null) return material;

                return null;
            } else {
                String[] split = string.split(":");

                material = Material.getMaterial(Integer.valueOf(split[0]));

                if(material != null) return material;

                return null;
            }
        }

        return material;
    }

    public static final ItemStack addGlow(ItemStack itemStack) {
        return GlowEnchant.addGlow(itemStack);
    }

}