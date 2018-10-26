package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.RandomUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class BossDropTableManager {

    private ItemsFileManager itemsFileManager;

    public BossDropTableManager(CustomBosses plugin) {
        this.itemsFileManager = plugin.getItemStackManager();
    }

    public List<ItemStack> getSprayItems(SprayTableElement sprayTableElement) {
        Map<String, Double> rewards = sprayTableElement.getSprayRewards();
        List<ItemStack> customDrops = new ArrayList<>();
        Integer maxDropsElement = sprayTableElement.getSprayMaxDrops();

        if(maxDropsElement == null) maxDropsElement = -1;

        final int maxDrops = maxDropsElement;

        for(Map.Entry<String, Double> entry : rewards.entrySet()) {
            if(maxDrops > 0) {
                if(customDrops.size() >= maxDrops) return customDrops;
            }

            double chance = entry.getValue();
            String itemName = entry.getKey();
            double randomNumber = RandomUtils.get().getRandomDecimalNumber();

            if(randomNumber > chance) continue;

            ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack(itemName);

            if(itemStackHolder == null) {
                Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(itemName);
                continue;
            }

            ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);

            if(itemStack == null) {
                Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(itemName);
                continue;
            }

            customDrops.add(itemStack);
        }

        return customDrops;
    }

}
