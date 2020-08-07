package com.songoda.epicbosses.utils.itemstack;

import com.songoda.epicbosses.utils.IReplaceableConverter;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolderJson;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class ItemStackConverter implements IReplaceableConverter<ItemStackHolder, ItemStack> {

    @Override
    public ItemStackHolder to(ItemStack itemStack) {
        try {
            return new ItemStackHolderJson(new ItemSerializer().serializeItemStackToJson(itemStack));
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public ItemStack from(ItemStackHolder itemStackHolder) {
        return from(itemStackHolder, null);
    }

    @Override
    public ItemStack from(ItemStackHolder itemStackHolder, Map<String, String> replaceMap) {
        ItemStack itemStack = new ItemStack(Material.AIR);

        if (itemStackHolder == null) return itemStack;
        
        ItemStack loaded = itemStackHolder.getItemStack();
        if (loaded == null) return itemStack;
        itemStack = loaded;
        
        if(itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            //-----------
            // SET NAME
            //-----------
            if (itemMeta.hasDisplayName()) {
                String name = StringUtils.get().translateColor(itemMeta.getDisplayName());

                itemMeta.setDisplayName(replaceString(name, replaceMap));
            }

            //-----------
            // SET LORE
            //-----------
            if (itemMeta.hasLore()) {
                List<String> replacedLore = new ArrayList<>(itemMeta.getLore());

                replacedLore.replaceAll(s -> s.replace('&', '§'));
                replacedLore.replaceAll(s -> replaceString(s, replaceMap));

                itemMeta.setLore(replacedLore);
            }
        }
        return itemStack;
    }

    private String replaceString(String input, Map<String, String> replaceMap) {
        if (replaceMap == null) return input;

        for (String replaceKey : replaceMap.keySet()) {
            if (input.contains(replaceKey)) {
                input = input.replace(replaceKey, replaceMap.get(replaceKey));
            }
        }

        return input;
    }
}
