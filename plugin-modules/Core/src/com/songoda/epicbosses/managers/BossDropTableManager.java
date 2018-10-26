package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.holder.DeadBossHolder;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.RandomUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class BossDropTableManager {

    private ItemsFileManager itemsFileManager;
    private BossEntityManager bossEntityManager;

    public BossDropTableManager(CustomBosses plugin) {
        this.itemsFileManager = plugin.getItemStackManager();
        this.bossEntityManager = plugin.getBossEntityManager();
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

    public void handleGiveTable(GiveTableElement giveTableElement, DeadBossHolder deadBossHolder) {
        Map<String, Map<String, GiveTableSubElement>> rewards = giveTableElement.getGiveRewards();
        Map<UUID, Double> mapOfDamage = deadBossHolder.getSortedDamageMap();
        List<UUID> positions = new ArrayList<>(mapOfDamage.keySet());

        rewards.forEach((positionString, lootMap) -> {
            if(!NumberUtils.get().isInt(positionString)) {
                Debug.DROP_TABLE_FAILED_INVALID_NUMBER.debug(positionString);
                return;
            }

            int position = NumberUtils.get().getInteger(positionString);

            if(positions.size() < position) return;

            UUID uuid = positions.get(position);
            double percentage = this.bossEntityManager.getPercentage(activeBossHolder, entry.getKey());

            lootMap.forEach((key, subElement) -> {
                Double requiredPercentage = subElement.getRequiredPercentage();
                Boolean randomDrops = subElement.getRandomDrops();

                if(requiredPercentage == null) requiredPercentage = 0.0D;
                if(randomDrops == null) randomDrops = false;


            });
        });
    }

}
