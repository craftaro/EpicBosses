package com.songoda.epicbosses.utils.itemstack.holder;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ItemStackHolder {

    @Expose
    private Integer amount;
    @Expose
    private String type;
    @Expose
    private Short durability;
    @Expose
    private String name;
    @Expose
    private List<String> lore;
    @Expose
    private List<String> enchants;
    @Expose
    private String skullOwner;
    @Expose
    private Short spawnerId;

    public ItemStackHolder(Integer amount, String type, Short durability, String name, List<String> lore, List<String> enchants, String skullOwner, Short spawnerId) {
        this.amount = amount;
        this.type = type;
        this.durability = durability;
        this.name = name;
        this.lore = lore;
        this.enchants = enchants;
        this.skullOwner = skullOwner;
        this.spawnerId = spawnerId;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public String getType() {
        return this.type;
    }

    public Short getDurability() {
        return this.durability;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public List<String> getEnchants() {
        return this.enchants;
    }

    public String getSkullOwner() {
        return this.skullOwner;
    }

    public Short getSpawnerId() {
        return this.spawnerId;
    }
}
