package com.songoda.epicbosses.utils.itemstack.holder;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ItemStackHolder {

    @Expose @Getter private Integer amount;
    @Expose @Getter private String type;
    @Expose @Getter private Short durability;
    @Expose @Getter private String name;
    @Expose @Getter private List<String> lore;
    @Expose @Getter private List<String> enchants;
    @Expose @Getter private String skullOwner;
    @Expose @Getter private Short spawnerId;
    @Expose @Getter private Boolean isGlowing;

    public ItemStackHolder(Integer amount, String type, Short durability, String name, List<String> lore, List<String> enchants, String skullOwner, Short spawnerId, Boolean isGlowing) {
        this.amount = amount;
        this.type = type;
        this.durability = durability;
        this.name = name;
        this.lore = lore;
        this.enchants = enchants;
        this.skullOwner = skullOwner;
        this.spawnerId = spawnerId;
        this.isGlowing = isGlowing;
    }

}
