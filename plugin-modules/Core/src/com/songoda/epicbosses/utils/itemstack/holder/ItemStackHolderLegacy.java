package com.songoda.epicbosses.utils.itemstack.holder;

import com.google.gson.annotations.Expose;
import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.converters.EnchantConverter;
import java.util.ArrayList;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ItemStackHolderLegacy implements ItemStackHolder {

    public static final EnchantConverter enchantConverter = new EnchantConverter();
    
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
    private EntityType spawnerId;

    public ItemStackHolderLegacy(Integer amount, String type, Short durability, String name, List<String> lore, List<String> enchants, String skullOwner, EntityType spawnerId) {
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

    public EntityType getSpawnerId() {
        return this.spawnerId;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.AIR);

        if (this.type == null) return itemStack;

        CompatibleMaterial cMaterial = CompatibleMaterial.getMaterial(this.type);
        Material material = cMaterial.getMaterial();

        if (material == null) return itemStack;

        itemStack.setType(material);

        if (this.durability != null && this.durability != -1) itemStack.setDurability(this.durability);
        if (this.enchants != null) itemStack.addUnsafeEnchantments(ItemStackHolderLegacy.enchantConverter.from(this.enchants));

        if (this.name != null || this.skullOwner != null || this.lore != null || this.spawnerId != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            //-----------
            // SET NAME
            //-----------
            if (this.name != null) {
                String name = StringUtils.get().translateColor(this.name);
                itemMeta.setDisplayName(name);
            }

            //-----------
            // SET LORE
            //-----------
            if (this.lore != null) {
                List<String> replacedLore = new ArrayList<>(this.lore);
                replacedLore.replaceAll(s -> s.replace('&', '§'));
                itemMeta.setLore(replacedLore);
            }

            //----------------------------------------------
            // SET OWNER, SPAWNER ID, OR UPDATE ITEM META
            //----------------------------------------------
            if (this.skullOwner != null) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                skullMeta.setOwner(this.skullOwner);
                itemStack.setItemMeta(skullMeta);
            } else if (this.spawnerId != null) {
                BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
                BlockState blockState = blockStateMeta.getBlockState();
                CreatureSpawner creatureSpawner = (CreatureSpawner) blockState;
                creatureSpawner.setSpawnedType(this.spawnerId);
                blockStateMeta.setBlockState(blockState);
                itemStack.setItemMeta(blockStateMeta);
            } else {
                itemStack.setItemMeta(itemMeta);
            }
        }

        if (amount != null && amount > 1) {
            itemStack.setAmount(amount);
        }

        return itemStack;
    }
}
