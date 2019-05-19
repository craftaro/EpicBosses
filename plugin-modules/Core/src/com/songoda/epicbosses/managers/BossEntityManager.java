package com.songoda.epicbosses.managers;

import com.google.gson.Gson;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.holder.DeadBossHolder;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.managers.files.MinionsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.custom.Minions;
import com.songoda.epicbosses.skills.elements.CustomMinionSkillElement;
import com.songoda.epicbosses.utils.BossesGson;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.RandomUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
    private static final String DEFAULT_BOSS_MENU_ITEM = "DefaultBossMenuItem";

    private MinionMechanicManager minionMechanicManager;
    private DropTableFileManager dropTableFileManager;
    private BossDropTableManager bossDropTableManager;
    private BossMechanicManager bossMechanicManager;
    private MinionsFileManager minionsFileManager;
    private ItemsFileManager bossItemFileManager;
    private BossesFileManager bossesFileManager;
    private BossTargetManager bossTargetManager;

    public BossEntityManager(CustomBosses customBosses) {
        this.minionMechanicManager = customBosses.getMinionMechanicManager();
        this.dropTableFileManager = customBosses.getDropTableFileManager();
        this.bossDropTableManager = customBosses.getBossDropTableManager();
        this.bossMechanicManager = customBosses.getBossMechanicManager();
        this.minionsFileManager = customBosses.getMinionsFileManager();
        this.bossItemFileManager = customBosses.getItemStackManager();
        this.bossesFileManager = customBosses.getBossesFileManager();
        this.bossTargetManager = customBosses.getBossTargetManager();
    }

    public double getRadius(ActiveBossHolder activeBossHolder, Location centerLocation) {
        if(activeBossHolder.isDead()) return Double.MAX_VALUE;

        LivingEntity livingEntity = activeBossHolder.getLivingEntity();

        if(livingEntity == null) return Double.MAX_VALUE;

        Location location = livingEntity.getLocation();

        return centerLocation.distance(location);
    }

    public Map<ActiveBossHolder, Double> getActiveBossHoldersWithinRadius(double radius, Location centerLocation) {
        Map<ActiveBossHolder, Double> distanceMap = new HashMap<>();

        getActiveBossHolders().forEach(activeBossHolder -> {
            double distance = getRadius(activeBossHolder, centerLocation);

            if(distance > radius) return;

            distanceMap.put(activeBossHolder, distance);
        });

        return distanceMap;
    }

    public int getCurrentlyActive(BossEntity bossEntity) {
        int amountOfBosses = 0;

        for(ActiveBossHolder activeBossHolder : getActiveBossHolders()) {
            if(activeBossHolder.getBossEntity().equals(bossEntity)) {
                amountOfBosses++;
            }
        }

        return amountOfBosses;
    }

    public int killAllHolders(World world) {
        int amountOfBosses = 0;

        for(ActiveBossHolder activeBossHolder : getActiveBossHolders()) {
            if(activeBossHolder.killAllSubBosses(world)) {
                activeBossHolder.killAllMinions(world);
                activeBossHolder.setDead(true);
                amountOfBosses++;

                ACTIVE_BOSS_HOLDERS.remove(activeBossHolder);
            }
        }

        CustomBosses.get().getAutoSpawnManager().clearAutoSpawns();

        return amountOfBosses;
    }

    public void killAllHolders(BossEntity bossEntity) {
        for(ActiveBossHolder activeBossHolder : getActiveBossHolders()) {
            if(activeBossHolder.getBossEntity().equals(bossEntity)) {
                activeBossHolder.killAll();
                activeBossHolder.killAllMinions();
                activeBossHolder.setDead(true);

                ACTIVE_BOSS_HOLDERS.remove(activeBossHolder);
            }
        }
    }

    public ItemStack getDisplaySpawnItem(BossEntity bossEntity) {
        if(bossEntity == null) return null;

        String spawnItemName = bossEntity.getSpawnItem() == null? DEFAULT_BOSS_MENU_ITEM : bossEntity.getSpawnItem();
        ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack(spawnItemName);

        if(itemStackHolder == null) {
            Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(spawnItemName);
            return null;
        }

        ItemStack itemStack = this.bossItemFileManager.getItemStackConverter().from(itemStackHolder);

        if(itemStack == null) {
            Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(spawnItemName);
            return null;
        }

        return itemStack;
    }

    public ItemStack getSpawnItem(BossEntity bossEntity) {
        if(bossEntity == null) return null;
        if(bossEntity.getSpawnItem() == null) return null;

        String spawnItemName = bossEntity.getSpawnItem();
        ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack(spawnItemName);

        if(itemStackHolder == null) {
            Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(spawnItemName);
            return null;
        }

        ItemStack itemStack = this.bossItemFileManager.getItemStackConverter().from(itemStackHolder);

        if(itemStack == null) {
            Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(spawnItemName);
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
        List<BossEntity> currentEntities = this.bossesFileManager.getBossEntities();
        Map<BossEntity, ItemStack> newMap = new HashMap<>();

        currentEntities.forEach(bossEntity -> newMap.put(bossEntity, getSpawnItem(bossEntity)));

        return newMap;
    }

    public ActiveBossHolder createActiveBossHolder(BossEntity bossEntity, Location spawnLocation, String name) {
        ActiveBossHolder activeBossHolder = new ActiveBossHolder(bossEntity, spawnLocation, name);

        this.bossMechanicManager.handleMechanicApplication(bossEntity, activeBossHolder);

        ACTIVE_BOSS_HOLDERS.add(activeBossHolder);
        return activeBossHolder;
    }

    public void spawnMinionsOnBossHolder(ActiveBossHolder activeBossHolder, Skill skill, CustomMinionSkillElement minionSkillElement) {
        String minionToSpawn = minionSkillElement.getMinionToSpawn();
        Integer amount = minionSkillElement.getAmount();

        if(minionToSpawn == null || minionToSpawn.isEmpty()) {
            Debug.FAILED_TO_SPAWN_MINIONS_FROM_SKILL.debug(skill.getDisplayName());
            return;
        }

        if(amount == null) amount = 1;

        MinionEntity minionEntity = this.minionsFileManager.getMinionEntity(minionToSpawn);
        Location location = activeBossHolder.getLivingEntity().getLocation();

        if(minionEntity == null) {
            Debug.FAILED_TO_FIND_MINION.debug(skill.getDisplayName(), minionToSpawn);
            return;
        }

        activeBossHolder.killAllMinions();

        for(int i = 1; i <= amount; i++) {
            ActiveMinionHolder activeMinionHolder = new ActiveMinionHolder(activeBossHolder, minionEntity, location, minionToSpawn);

            this.minionMechanicManager.handleMechanicApplication(minionEntity, activeMinionHolder);
            this.bossTargetManager.handleMinionTargeting(activeMinionHolder);

            activeBossHolder.getActiveMinionHolderMap().put(i, activeMinionHolder);
        }

        activeBossHolder.getActiveMinionHolderMap().values().forEach(activeMinionHolder -> activeMinionHolder.getTargetHandler().runTargetCycle());
    }

    public ActiveBossHolder getActiveBossHolder(LivingEntity livingEntity) {
        List<ActiveBossHolder> currentList = getActiveBossHolders();

        for(ActiveBossHolder activeBossHolder : currentList) {
            for(Map.Entry<Integer, UUID> entry : activeBossHolder.getLivingEntityMap().entrySet()) {
                if(entry.getValue().equals(livingEntity.getUniqueId())) return activeBossHolder;
            }
        }

        return null;
    }

    public void removeActiveBossHolder(ActiveBossHolder activeBossHolder) {
        for(Map.Entry<Integer, UUID> entry : activeBossHolder.getLivingEntityMap().entrySet()) {
            LivingEntity livingEntity = (LivingEntity) ServerUtils.get().getEntity(entry.getValue());
            if (livingEntity != null && !livingEntity.isDead())
                livingEntity.remove();
        }

        ACTIVE_BOSS_HOLDERS.remove(activeBossHolder);
    }

    public boolean isAllEntitiesDead(ActiveBossHolder activeBossHolder) {
        for(Map.Entry<Integer, UUID> entry : activeBossHolder.getLivingEntityMap().entrySet()) {
            LivingEntity livingEntity = (LivingEntity) ServerUtils.get().getEntity(entry.getValue());
            if (livingEntity != null && !livingEntity.isDead())
                return false;
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

        Gson gson = BossesGson.get();

        if(dropType.equalsIgnoreCase("SPRAY")) {
            SprayTableElement sprayTableElement = dropTable.getSprayTableData();
            List<ItemStack> itemStacks = this.bossDropTableManager.getSprayItems(sprayTableElement);

            sprayDrops(sprayTableElement, itemStacks, deadBossHolder);
        } else if(dropType.equalsIgnoreCase("GIVE")) {
            GiveTableElement giveTableElement = dropTable.getGiveTableData();

            this.bossDropTableManager.handleGiveTable(giveTableElement, deadBossHolder);
        } else if(dropType.equalsIgnoreCase("DROP")) {
            DropTableElement dropTableElement = dropTable.getDropTableData();
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

    private List<ActiveBossHolder> getActiveBossHolders() {
        return new ArrayList<>(ACTIVE_BOSS_HOLDERS);
    }

}
