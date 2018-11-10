package com.songoda.epicbosses.skills.custom;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.elements.CustomCageSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkill;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.converters.MaterialConverter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 06-Nov-18
 */
public class Cage extends CustomSkill {

    @Getter private static final Map<UUID, Map<String, Queue<BlockState>>> mapOfCages = new HashMap<>(), mapOfRestoreCages = new HashMap<>();
            private static final MaterialConverter MATERIAL_CONVERTER = new MaterialConverter();
    @Getter private static final Map<Location, Integer> mapOfCagesOnLocation = new HashMap<>();
    @Getter private static final Map<Location, BlockState> mapOfOldBlocks = new HashMap<>();
    @Getter private static final List<UUID> playersInCage = new ArrayList<>();


    @Expose @Getter @Setter private CustomCageSkillElement cage;

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        nearbyEntities.forEach(livingEntity -> {
            UUID uuid = livingEntity.getUniqueId();

            if(getPlayersInCage().contains(uuid)) return;

            getPlayersInCage().add(uuid);

            Location teleportLocation = getTeleportLocation(livingEntity);
            Map<String, Queue<BlockState>> originalMap = getBlockStateMap(teleportLocation);

            livingEntity.teleport(teleportLocation);
            getMapOfCages().put(uuid, originalMap);
            getMapOfRestoreCages().put(uuid, new HashMap<>(originalMap));

            ServerUtils.get().runLater(1L, () -> setCageBlocks(uuid));
            ServerUtils.get().runLater(100L, () -> {
                restoreCageBlocks(uuid);
                getPlayersInCage().remove(uuid);
            });
        });
    }

    private void restoreCageBlocks(UUID uuid) {
        Map<String, Queue<BlockState>> queueMap = getMapOfRestoreCages().get(uuid);

        restoreBlocks(queueMap.get("W"));
        restoreBlocks(queueMap.get("F"));
        restoreBlocks(queueMap.get("I"));
    }

    private void restoreBlocks(Queue<BlockState> queue) {
        queue.forEach(blockState -> {
            if(blockState == null) return;

            Location location = blockState.getLocation();
            int amountOfCages = getMapOfCagesOnLocation().getOrDefault(location, 1);

            if(amountOfCages == 1) {
                BlockState oldState = getMapOfOldBlocks().get(location);

                location.getBlock().setType(oldState.getType());
                location.getBlock().setBlockData(oldState.getBlockData());
                getMapOfCagesOnLocation().remove(location);
            } else {
                getMapOfCagesOnLocation().put(location, amountOfCages-1);
            }
        });
    }

    private void setCageBlocks(UUID uuid) {
        Map<String, Queue<BlockState>> queueMap = getMapOfCages().get(uuid);

        setBlocks(queueMap.get("W"), getCage().getWallType());
        setBlocks(queueMap.get("F"), getCage().getFlatType());
        setBlocks(queueMap.get("I"), getCage().getInsideType());
    }

    private void setBlocks(Queue<BlockState> queue, String materialType) {
        Material material = MATERIAL_CONVERTER.from(materialType);

        if(material == null) {
            Debug.SKILL_CAGE_INVALID_MATERIAL.debug(materialType, getDisplayName());
            return;
        }

        queue.forEach(blockState -> {
            if(blockState == null) return;

            Location location = blockState.getLocation();
            int currentAmount = getMapOfCagesOnLocation().getOrDefault(location, 0);

            if(!getMapOfOldBlocks().containsKey(location)) getMapOfOldBlocks().put(location, blockState);

            blockState.getBlock().setType(material);
            getMapOfCagesOnLocation().put(location, currentAmount+1);
        });
    }

    private Map<String, Queue<BlockState>> getBlockStateMap(Location location) {
        Map<String, Queue<BlockState>> map = new HashMap<>();

        map.put("W", getCageWalls(location));
        map.put("F", getCageFlats(location));
        map.put("I", getCageInside(location));

        return map;
    }

    private Location getTeleportLocation(LivingEntity livingEntity) {
        Location currentLocation = livingEntity.getLocation();

        return currentLocation.clone().add(0.5, 0, 0.5);
    }

    private Queue<BlockState> getCageFlats(Location playerLocation) {
        World world = playerLocation.getWorld();
        Queue<Location> locationQueue = new LinkedList<>();

        for(int x = 1; x >= -1; x--) {
            for(int z = 1; z >= -1; z--) {
                Location location1 = new Location(world, x, +2, z);
                Location location2 = new Location(world, x, -1, z);

                locationQueue.add(location1);
                locationQueue.add(location2);
            }
        }

        locationQueue.add(new Location(world, +1, +2, -1));
        locationQueue.add(new Location(world, +1, +2, +0));

        return sortLocationQueue(locationQueue, playerLocation);
    }

    private Queue<BlockState> getCageInside(Location playerLocation) {
        World world = playerLocation.getWorld();
        Queue<Location> locationQueue = new LinkedList<>();

        for(int y = 1; y >= 0; y--) {
            Location innerLocation = new Location(world, 0, y, 0);

            locationQueue.add(innerLocation);
        }

        return sortLocationQueue(locationQueue, playerLocation);
    }

    public Queue<BlockState> getCageWalls(Location playerLocation) {
        World world = playerLocation.getWorld();
        Queue<Location> locationQueue = new LinkedList<>();

        for(int x = 1; x >= -1; x--) {
            for(int z = 1; z >= -1; z--) {
                Location location1 = new Location(world, x, 1, z);
                Location location2 = new Location(world, x, 0, z);

                locationQueue.add(location1);
                locationQueue.add(location2);
            }
        }

        return sortLocationQueue(locationQueue, playerLocation);
    }

    private Queue<BlockState> sortLocationQueue(Queue<Location> queue, Location playerLocation) {
        Queue<BlockState> blockStateQueue = new LinkedList<>();
        World world = playerLocation.getWorld();

        while(!queue.isEmpty()) {
            Location temp = queue.poll();

            if(temp == null) continue;

            Block block = world.getBlockAt(temp.add(playerLocation).clone());

            blockStateQueue.add(block.getState());
        }

        return blockStateQueue;
    }
}
