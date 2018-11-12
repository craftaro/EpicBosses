package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.holder.DeadBossHolder;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.skills.custom.Minions;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.RandomUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class BossEntityManager {

    private static final List<ActiveBossHolder> ACTIVE_BOSS_HOLDERS = new ArrayList<>();

    private MinionMechanicManager minionMechanicManager;
    private DropTableFileManager dropTableFileManager;
    private BossDropTableManager bossDropTableManager;
    private BossMechanicManager bossMechanicManager;
    private ItemsFileManager bossItemFileManager;
    private BossesFileManager bossesFileManager;

    public BossEntityManager(CustomBosses customBosses) {
        this.minionMechanicManager = customBosses.getMinionMechanicManager();
        this.dropTableFileManager = customBosses.getDropTableFileManager();
        this.bossDropTableManager = customBosses.getBossDropTableManager();
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

    public List<String> getOnSpawnMessage(BossEntity bossEntity) {
        String id = bossEntity.getMessages().getOnSpawn().getMessage();
        List<String> messages = BossAPI.getStoredMessages(id);

        if(messages == null) {
            Debug.FAILED_TO_LOAD_MESSAGES.debug(id);
            return null;
        }

        return messages;
    }

    public int getOnSpawnMessageRadius(BossEntity bossEntity) {
        Integer radius = bossEntity.getMessages().getOnSpawn().getRadius();

        if(radius == null) radius = -1;

        return radius;
    }

    public List<String> getOnSpawnCommands(BossEntity bossEntity) {
        String id = bossEntity.getCommands().getOnSpawn();
        List<String> commands = BossAPI.getStoredCommands(id);

        if(commands == null) {
            Debug.FAILED_TO_LOAD_COMMANDS.debug(id);
            return null;
        }

        return commands;
    }

    public List<String> getOnDeathMessage(BossEntity bossEntity) {
        String id = bossEntity.getMessages().getOnDeath().getMessage();
        List<String> messages = BossAPI.getStoredMessages(id);

        if(messages == null) {
            Debug.FAILED_TO_LOAD_MESSAGES.debug(id);
            return null;
        }

        return messages;
    }

    public int getOnDeathMessageRadius(BossEntity bossEntity) {
        Integer radius = bossEntity.getMessages().getOnDeath().getRadius();

        if(radius == null) radius = -1;

        return radius;
    }

    public int getOnDeathShowAmount(BossEntity bossEntity) {
        Integer onlyShow = bossEntity.getMessages().getOnDeath().getOnlyShow();

        if(onlyShow == null) onlyShow = 3;

        return onlyShow;
    }

    public List<String> getOnDeathPositionMessage(BossEntity bossEntity) {
        String id = bossEntity.getMessages().getOnDeath().getPositionMessage();
        List<String> messages = BossAPI.getStoredMessages(id);

        if(messages == null) {
            Debug.FAILED_TO_LOAD_MESSAGES.debug(id);
            return null;
        }

        return messages;
    }

    public List<String> getOnDeathCommands(BossEntity bossEntity) {
        String id = bossEntity.getCommands().getOnDeath();
        List<String> commands = BossAPI.getStoredCommands(id);

        if(commands == null) {
            Debug.FAILED_TO_LOAD_COMMANDS.debug(id);
            return null;
        }

        return commands;
    }

    public Map<BossEntity, ItemStack> getMapOfEntitiesAndSpawnItems() {
        Map<String, BossEntity> currentEntities = new HashMap<>(this.bossesFileManager.getBossEntities());
        Map<BossEntity, ItemStack> newMap = new HashMap<>();

        currentEntities.forEach((name, bossEntity) -> newMap.put(bossEntity, getSpawnItem(bossEntity)));

        return newMap;
    }

    public ActiveBossHolder createActiveBossHolder(BossEntity bossEntity, Location spawnLocation, String name) {
        ActiveBossHolder activeBossHolder = new ActiveBossHolder(bossEntity, spawnLocation, name);

        if(!this.bossMechanicManager.handleMechanicApplication(bossEntity, activeBossHolder)) {
            Debug.FAILED_TO_CREATE_ACTIVE_BOSS_HOLDER.debug();
            return null;
        }

        ACTIVE_BOSS_HOLDERS.add(activeBossHolder);
        return activeBossHolder;
    }

    public ActiveBossHolder spawnMinionsOnBossHolder(ActiveBossHolder activeBossHolder, MinionEntity minionEntity, Minions minions) {
        //TODO: Add Minions json class
        //TODO: Finish minions spawn method

        if(!this.minionMechanicManager.handleMechanicApplication(minionEntity, activeBossHolder)) {
            Debug.FAILED_TO_CREATE_ACTIVE_BOSS_HOLDER.debug();
            return null;
        }

        return activeBossHolder;
    }

    public ActiveBossHolder getActiveBossHolder(LivingEntity livingEntity) {
        List<ActiveBossHolder> currentList = new ArrayList<>(ACTIVE_BOSS_HOLDERS);

        for(ActiveBossHolder activeBossHolder : currentList) {
            for(Map.Entry<Integer, LivingEntity> entry : activeBossHolder.getLivingEntityMap().entrySet()) {
                if(entry.getValue().getUniqueId().equals(livingEntity.getUniqueId())) return activeBossHolder;
            }
        }

        return null;
    }

    public void removeActiveBossHolder(ActiveBossHolder activeBossHolder) {
        for(Map.Entry<Integer, LivingEntity> entry : activeBossHolder.getLivingEntityMap().entrySet()) {
            if(!entry.getValue().isDead()) entry.getValue().remove();
        }

        ACTIVE_BOSS_HOLDERS.remove(activeBossHolder);
    }

    public boolean isAllEntitiesDead(ActiveBossHolder activeBossHolder) {
        for(Map.Entry<Integer, LivingEntity> entry : activeBossHolder.getLivingEntityMap().entrySet()) {
            if(!entry.getValue().isDead()) return false;
        }

        return true;
    }

    public Map<UUID, Double> getSortedMapOfDamage(ActiveBossHolder activeBossHolder) {
        Map<UUID, Double> unsortedMap = activeBossHolder.getMapOfDamagingUsers();
        Map<UUID, Double> sortedMap;

        sortedMap = unsortedMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return sortedMap;
    }

    public Map<UUID, Double> getPercentageMap(Map<UUID, Double> damagingUsers) {
        Map<UUID, Double> percentageMap = new HashMap<>();
        double totalDamage = 0.0;

        for(Double damage : damagingUsers.values()) {
            if(damage != null) totalDamage += damage;
        }

        double onePercent = totalDamage / 100;

        damagingUsers.forEach((uuid, damage) -> {
            if(uuid == null || damage == null) return;

            double playerPercent = damage / onePercent;

            percentageMap.put(uuid, playerPercent);
        });

        return percentageMap;
    }

    public DropTable getDropTable(BossEntity bossEntity) {
        return this.dropTableFileManager.getDropTable(bossEntity.getDrops().getDropTable());
    }

    public void handleDropTable(DropTable dropTable, DeadBossHolder deadBossHolder) {
        String dropType = dropTable.getDropType();
        BossEntity bossEntity = deadBossHolder.getBossEntity();
        String tableName = bossEntity.getDrops().getDropTable();

        if(dropType == null) {
            Debug.FAILED_TO_FIND_DROP_TABLE_TYPE.debug(tableName);
            return;
        }

        if(dropType.equalsIgnoreCase("SPRAY")) {
            SprayTableElement sprayTableElement = (SprayTableElement) dropTable.getRewards();
            List<ItemStack> itemStacks = this.bossDropTableManager.getSprayItems(sprayTableElement);

            sprayDrops(sprayTableElement, itemStacks, deadBossHolder);
        } else if(dropType.equalsIgnoreCase("GIVE")) {
            GiveTableElement giveTableElement = (GiveTableElement) dropTable.getRewards();

            this.bossDropTableManager.handleGiveTable(giveTableElement, deadBossHolder);
        } else if(dropType.equalsIgnoreCase("DROP")) {
            DropTableElement dropTableElement = (DropTableElement) dropTable.getRewards();
            List<ItemStack> itemStacks = this.bossDropTableManager.getDropItems(dropTableElement);

            itemStacks.forEach(itemStack -> deadBossHolder.getLocation().getWorld().dropItemNaturally(deadBossHolder.getLocation(), itemStack));
        } else {
            Debug.FAILED_TO_FIND_DROP_TABLE_TYPE.debug(tableName);
        }
    }

    private void sprayDrops(SprayTableElement sprayTableElement, List<ItemStack> rewards, DeadBossHolder deadBossHolder) {
        Integer maximumDistance = sprayTableElement.getSprayMaxDistance();

        if(maximumDistance == null) maximumDistance = 10;

        Location deathLocation = deadBossHolder.getLocation();
        Integer finalMaximumDistance = maximumDistance;

        rewards.forEach(itemStack -> {
            Location destinationLocation = deathLocation.clone();
            int x = RandomUtils.get().getRandomNumber(finalMaximumDistance + 1);
            int currentX = destinationLocation.getBlockX();
            int z = RandomUtils.get().getRandomNumber(finalMaximumDistance + 1);
            int currentZ = destinationLocation.getBlockZ();

            if(RandomUtils.get().preformRandomAction()) x = -x;
            if(RandomUtils.get().preformRandomAction()) z = -z;

            destinationLocation.setX(currentX + x);
            destinationLocation.setZ(currentZ + z);

            Item item = deathLocation.getWorld().dropItemNaturally(deathLocation, itemStack);
            Vector vector = deathLocation.toVector().subtract(destinationLocation.toVector()).normalize();

            item.setPickupDelay(20);
            item.setVelocity(vector);
        });
    }

}
