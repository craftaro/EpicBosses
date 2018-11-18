package com.songoda.epicbosses.utils.itemstack;

import com.songoda.epicbosses.utils.IReplaceableConverter;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.factory.NbtFactory;
import com.songoda.epicbosses.utils.itemstack.converters.EnchantConverter;
import com.songoda.epicbosses.utils.itemstack.converters.MaterialConverter;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
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
public class ItemStackConverter implements IReplaceableConverter<ItemStackHolder, ItemStack> {

    private MaterialConverter materialConverter;
    private EnchantConverter enchantConverter;

    public ItemStackConverter() {
        this.materialConverter = new MaterialConverter();
        this.enchantConverter = new EnchantConverter();
    }

    @Override
    public ItemStackHolder to(ItemStack itemStack) {
        Material material = itemStack.getType();
        int amount = itemStack.getAmount();
        Short durability = itemStack.getDurability(), spawnerId = null;
        String type, name = null, skullOwner = null;
        List<String> lore = null, enchants = null;
        Boolean isGlowing = null;

        if(durability == 0) {
            durability = null;
        }

        type = this.materialConverter.to(material);

        if(itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if(itemMeta.hasDisplayName()) {
                name = itemMeta.getDisplayName().replace('§', '&');
            }

            if(itemMeta.hasLore()) {
                lore = new ArrayList<>();

                for(String string : itemMeta.getLore()) {
                    lore.add(string.replace('§', '&'));
                }
            }

            if(itemMeta.hasEnchants()) {
                enchants = this.enchantConverter.to(itemMeta.getEnchants());
            }

            if(itemMeta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;

                if(skullMeta.hasOwner()) {
                    skullOwner = skullMeta.getOwner();
                }
            }

            if(itemMeta instanceof BlockStateMeta) {
                BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
                BlockState blockState = blockStateMeta.getBlockState();

                if(blockState instanceof CreatureSpawner) {
                    CreatureSpawner creatureSpawner = (CreatureSpawner) blockState;

                    spawnerId = creatureSpawner.getSpawnedType().getTypeId();
                }
            }
        }

//        if(enchants == null) {
//            ItemStack craftStack = NbtFactory.getCraftItemStack(itemStack);
//            NbtFactory.NbtCompound compound = NbtFactory.fromItemTag(craftStack);
//
//            if(compound.containsKey("ench")) isGlowing = true;
//        }

        return new ItemStackHolder(amount, type, durability, name, lore, enchants, skullOwner, spawnerId, false);
    }

    @Override
    public ItemStack from(ItemStackHolder itemStackHolder) {
        return from(itemStackHolder, null);
    }

    @Override
    public ItemStack from(ItemStackHolder itemStackHolder, Map<String, String> replaceMap) {
        ItemStack itemStack = new ItemStack(Material.AIR);

        if(itemStackHolder.getType() == null) return itemStack;

        Material material = this.materialConverter.from(itemStackHolder.getType());

        if(material == null) return itemStack;

        itemStack.setType(material);

        Integer amount = itemStackHolder.getAmount();
        Short durability = itemStackHolder.getDurability(), spawnerId = itemStackHolder.getSpawnerId();
        String name = itemStackHolder.getName(), skullOwner = itemStackHolder.getSkullOwner();
        List<String> lore = itemStackHolder.getLore(), enchants = itemStackHolder.getEnchants();
        Boolean isGlowing = itemStackHolder.getIsGlowing();

        if(durability != null) itemStack.setDurability(durability);
        if(enchants != null) itemStack.addUnsafeEnchantments(this.enchantConverter.from(enchants));

        if(name != null || skullOwner != null || lore != null || spawnerId != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            //-----------
            // SET NAME
            //-----------
            if(name != null) {
                name = StringUtils.get().translateColor(name);

                itemMeta.setDisplayName(replaceString(name, replaceMap));
            }

            //-----------
            // SET LORE
            //-----------
            if(lore != null) {
                List<String> replacedLore = new ArrayList<>(lore);

                replacedLore.replaceAll(s -> s.replace('&', '§'));
                replacedLore.replaceAll(s -> replaceString(s, replaceMap));

                itemMeta.setLore(replacedLore);
            }

            //----------------------------------------------
            // SET OWNER, SPAWNER ID, OR UPDATE ITEM META
            //----------------------------------------------
            if(skullOwner != null) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;

                skullMeta.setOwner(skullOwner);
                itemStack.setItemMeta(skullMeta);
            } else if(spawnerId != null) {
                BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
                BlockState blockState = blockStateMeta.getBlockState();
                CreatureSpawner creatureSpawner = (CreatureSpawner) blockState;

                creatureSpawner.setSpawnedType(EntityType.fromId(spawnerId));
                blockStateMeta.setBlockState(blockState);
                itemStack.setItemMeta(blockStateMeta);
            } else {
                itemStack.setItemMeta(itemMeta);
            }
        }

        if(isGlowing != null && isGlowing) {
            ItemStack craftStack = NbtFactory.getCraftItemStack(itemStack);
            NbtFactory.NbtCompound compound = NbtFactory.fromItemTag(craftStack);

            compound.put("ench", NbtFactory.createList());
            return craftStack;
        }

        if(amount != null && amount > 1) {
            itemStack.setAmount(amount);
        }

        return itemStack;
    }

    private String replaceString(String input, Map<String, String> replaceMap) {
        if(replaceMap == null) return input;

        for(String replaceKey : replaceMap.keySet()) {
            if(input.contains(replaceKey)) {
                input = input.replace(replaceKey, replaceMap.get(replaceKey));
            }
        }

        return input;
    }
}
