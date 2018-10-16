package net.aminecraftdev.custombosses.managers;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.managers.files.BossItemFileManager;
import net.aminecraftdev.custombosses.managers.files.BossesFileManager;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class BossEntityManager {

    private static final List<ActiveBossHolder> ACTIVE_BOSS_HOLDERS = new ArrayList<>();

    private BossItemFileManager bossItemFileManager;
    private BossMechanicManager bossMechanicManager;
    private BossesFileManager bossesFileManager;

    public BossEntityManager(CustomBosses customBosses) {
        this.bossMechanicManager = customBosses.getBossMechanicManager();
        this.bossItemFileManager = customBosses.getItemStackManager();
        this.bossesFileManager = customBosses.getBossesFileManager();
    }

    public ItemStack getSpawnItem(BossEntity bossEntity) {
        ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack(bossEntity.getSpawnItem());

        if(itemStackHolder == null) {
            Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(bossEntity.getSpawnItem());
            return null;
        }

        ItemStack itemStack = this.bossItemFileManager.getItemStackConverter().from(itemStackHolder);

        if(itemStack == null) {
            Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(bossEntity.getSpawnItem());
            return null;
        }

        return itemStack;
    }

    public Map<BossEntity, ItemStack> getMapOfEntitiesAndSpawnItems() {
        Map<String, BossEntity> currentEntities = new HashMap<>(this.bossesFileManager.getBossEntities());
        Map<BossEntity, ItemStack> newMap = new HashMap<>();

        currentEntities.forEach((name, bossEntity) -> newMap.put(bossEntity, getSpawnItem(bossEntity)));

        return newMap;
    }

    public ActiveBossHolder createActiveBossHolder(BossEntity bossEntity, Location spawnLocation) {
        ActiveBossHolder activeBossHolder = new ActiveBossHolder(bossEntity, spawnLocation);

        if(!this.bossMechanicManager.handleMechanicApplication(bossEntity, activeBossHolder)) {
            Debug.FAILED_TO_CREATE_ACTIVE_BOSS_HOLDER.debug();
            return null;
        }

        ACTIVE_BOSS_HOLDERS.add(activeBossHolder);
        return activeBossHolder;
    }

}
