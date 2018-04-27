package net.aminecraftdev.custombosses.utils.itemstack.holder;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class ItemStackHolder {

    @Expose private String type;
    @Expose private Short durability;
    @Expose private String name;
    @Expose private List<String> lore;
    @Expose private List<String> enchants;
    @Expose private String skullOwner;
    @Expose private Short spawnerId;
    @Expose private Boolean glowing;

    public ItemStackHolder(String type, Short durability, String name, List<String> lore, List<String> enchants, String skullOwner, Short spawnerId, Boolean glowing) {
        this.type = type;
        this.durability = durability;
        this.name = name;
        this.lore = lore;
        this.enchants = enchants;
        this.skullOwner = skullOwner;
        this.spawnerId = spawnerId;
        this.glowing = glowing;
    }

    public String getType() {
        return type;
    }

    public Short getDurability() {
        return durability;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getEnchants() {
        return enchants;
    }

    public String getSkullOwner() {
        return skullOwner;
    }

    public Short getSpawnerId() {
        return spawnerId;
    }

    public Boolean getIsGlowing() {
        return glowing;
    }
}
