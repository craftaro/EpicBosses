package com.songoda.epicbosses.utils.itemstack.holder;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.utils.itemstack.ItemSerializer;

import java.util.List;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ItemStackHolderJson implements ItemStackHolder {

    private static final ItemSerializer serializer;

    static {
        ItemSerializer is;
        try {
            is = new ItemSerializer();
        } catch (Exception e) {
            is = null;
            e.printStackTrace();
        }
        serializer = is;
    }
    
    @Expose
    private final String json;

    public ItemStackHolderJson(String json) {
        this.json = json;
    }

    public String getJson() {
        return this.json;
    }

    public ItemStack getItemStack() {
        try {
            return serializer.deserializeItemStackFromJson(json);
        } catch (Exception e) {
            return null;
        }
    }
}
