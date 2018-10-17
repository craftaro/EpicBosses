package net.aminecraftdev.custombosses.utils.itemstack;

import net.aminecraftdev.custombosses.utils.NumberUtils;
import net.aminecraftdev.custombosses.utils.StringUtils;
import net.aminecraftdev.custombosses.utils.factory.NbtFactory;
import net.aminecraftdev.custombosses.utils.itemstack.enchants.GlowEnchant;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * Created by charl on 28-Apr-17.
 */
public class ItemStackUtils {

    public static ItemStack createItemStack(ItemStack itemStack, Map<String,String> replaceMap) {
        return createItemStack(itemStack, replaceMap, null);
    }

    public static ItemStack createItemStack(ItemStack itemStack, Map<String,String> replaceMap, Map<String,Object> compoundData) {
        ItemStack cloneStack = itemStack.clone();
        ItemMeta itemMeta = cloneStack.getItemMeta();
        boolean hasName = cloneStack.getItemMeta().hasDisplayName();
        boolean hasLore = cloneStack.getItemMeta().hasLore();
        String name = "";
        List<String> newLore = new ArrayList<>();

        if(hasName) name = cloneStack.getItemMeta().getDisplayName();

        if(replaceMap != null && !replaceMap.isEmpty()) {
            if(hasName) {
                for(String s : replaceMap.keySet()) {
                    name = name.replace(s, replaceMap.get(s));
                }

                itemMeta.setDisplayName(name);
            }

            if(hasLore) {
                for(String s : itemMeta.getLore()) {
                    for(String z : replaceMap.keySet()) {
                        if(s.contains(z)) s = s.replace(z, replaceMap.get(z));
                    }

                    newLore.add(s);
                }

                itemMeta.setLore(newLore);
            }
        }

        cloneStack.setItemMeta(itemMeta);

        if(compoundData == null || compoundData.isEmpty()) return cloneStack;

        ItemStack craftStack = NbtFactory.getCraftItemStack(cloneStack);
        NbtFactory.NbtCompound compound = NbtFactory.fromItemTag(craftStack);

        for(String s : compoundData.keySet()) {
            compound.put(s, compoundData.get(s));
        }

        return craftStack;
    }

    public static ItemStack createItemStack(ConfigurationSection configurationSection) {
        return createItemStack(configurationSection, 1, null);
    }

    public static ItemStack createItemStack(ConfigurationSection configurationSection, int amount, Map<String, String> replacedMap) {
        String type = configurationSection.getString("type");
        String name = configurationSection.getString("name");
        List<String> lore = configurationSection.getStringList("lore");
        List<String> enchants = configurationSection.getStringList("enchants");
        short durability = (short) configurationSection.getInt("durability");
        String owner = configurationSection.getString("owner");
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

                if(ench.equalsIgnoreCase("GLOW")) {
                    addGlow = true;
                } else {
                    int level = Integer.parseInt(spl[1]);

                    map.put(Enchantment.getByName(ench), level);
                }
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

        if(configurationSection.contains("owner") && itemStack.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

            skullMeta.setOwner(owner);

            itemStack.setItemMeta(skullMeta);
        }

        return addGlow? addGlow(itemStack) : itemStack;
    }

    public static void applyDisplayName(ItemStack itemStack, String name) {
        applyDisplayName(itemStack, name, null);
    }

    public static void applyDisplayName(ItemStack itemStack, String name, Map<String, String> replaceMap) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(replaceMap != null) {
            for(String s : replaceMap.keySet()) {
                if(name.contains(s)) name = name.replace(s, replaceMap.get(s));
            }
        }

        itemMeta.setDisplayName(StringUtils.get().translateColor(name));
        itemStack.setItemMeta(itemMeta);
    }

    public static void applyDisplayLore(ItemStack itemStack, List<String> lore) {
        applyDisplayLore(itemStack, lore, null);
    }

    public static void applyDisplayLore(ItemStack itemStack, List<String> lore, Map<String, String> replaceMap) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(replaceMap != null) {
            for(String s : replaceMap.keySet()) {
                lore.replaceAll(loreLine -> loreLine
                        .replace(s, replaceMap.get(s))
                        .replace('&', '§')
                );
            }
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    public static Material getType(String string) {
        Material material = Material.getMaterial(string);

        if(material == null) {
            if(NumberUtils.get().isInt(string)) {
                return null;
            } else {
                String[] split = string.split(":");

                material = Material.getMaterial(split[0]);

                if(material != null) return material;

                return null;
            }
        }

        return material;
    }

    public static ItemStack addGlow(ItemStack itemStack) {
        return GlowEnchant.addGlow(itemStack);
    }

    public static void giveItems(Player player, ItemStack... items) {
        giveItems(player, Arrays.asList(items));
    }

    public static void giveItems(Player player, List<ItemStack> items) {
        PlayerInventory inventory = player.getInventory();

        for(ItemStack itemStack : items) {
            int amount = itemStack.getAmount();

            while(amount > 0) {
                int toGive = amount > 64? 64 : amount;

                ItemStack stack = itemStack.clone();
                stack.setAmount(toGive);

                if(inventory.firstEmpty() != -1) {
                    inventory.addItem(stack);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), stack);
                }

                amount -= toGive;
            }
        }
    }

    public static void takeItems(Player player, Map<ItemStack, Integer> items) {
        PlayerInventory inventory = player.getInventory();

        for(ItemStack itemStack : items.keySet()) {
            int toTake = items.get(itemStack);
            int i = 0;

            while(toTake > 0 && i < inventory.getSize()) {
                if (inventory.getItem(i) != null && inventory.getItem(i).getType() == itemStack.getType() && (inventory.getItem(i).getData().getData() == itemStack.getData().getData() || itemStack.getData().getData() == -1)) {
                    ItemStack target = inventory.getItem(i);
                    if(target.getAmount() > toTake) {
                        target.setAmount(target.getAmount()-toTake);
                        inventory.setItem(i, target);
                        break;
                    } else if(target.getAmount() == toTake) {
                        inventory.setItem(i, new ItemStack(Material.AIR));
                        break;
                    } else {
                        toTake -= target.getAmount();
                        inventory.setItem(i, new ItemStack(Material.AIR));
                    }
                }
                i++;
            }
        }
    }

    public static int getAmount(Player player, ItemStack itemStack) {
        PlayerInventory playerInventory = player.getInventory();
        int amountInInventory = 0;

        for(int i = 0; i < playerInventory.getSize(); i++) {
            if (playerInventory.getItem(i) != null && playerInventory.getItem(i).getType() == itemStack.getType() && (playerInventory.getItem(i).getData().getData() == itemStack.getData().getData() || itemStack.getData().getData() == -1)) {
                amountInInventory += playerInventory.getItem(i).getAmount();
            }
        }

        return amountInInventory;
    }

    @SuppressWarnings("unchecked")
    public static ItemStackHolder getItemStackHolder(ConfigurationSection configurationSection) {
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

    public static boolean isItemStackSame(ItemStack itemStack1, ItemStack itemStack2) {
        if(itemStack1 == null || itemStack2 == null) return false;
        if(itemStack1.getType() != itemStack2.getType()) return false;
        if(itemStack1.getDurability() != itemStack2.getDurability()) return false;

        ItemMeta itemMeta1 = itemStack1.getItemMeta();
        ItemMeta itemMeta2 = itemStack2.getItemMeta();

        if(itemMeta1 == null || itemMeta2 == null) return false;
        if(itemMeta1.hasDisplayName() != itemMeta2.hasDisplayName()) return false;
        if(!itemMeta1.getDisplayName().equals(itemMeta2.getDisplayName())) return false;
        if(itemMeta1.hasLore() != itemMeta2.hasLore()) return false;
        if(!itemMeta1.getLore().equals(itemMeta2.getLore())) return false;

        return true;
    }
}