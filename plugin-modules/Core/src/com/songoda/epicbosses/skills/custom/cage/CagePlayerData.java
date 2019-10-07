package com.songoda.epicbosses.skills.custom.cage;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Nov-18
 */
public class CagePlayerData {

    private final Map<String, Queue<BlockState>> mapOfCages = new HashMap<>(), mapOfRestoreCages = new HashMap<>();
    private final UUID uuid;

    public CagePlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public void setBlockStateMaps(Location location) {
        getMapOfCages().put("W", getCageWalls(location));
        getMapOfCages().put("F", getCageFlats(location));
        getMapOfCages().put("I", getCageInside(location));

        getMapOfRestoreCages().putAll(getMapOfCages());
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

    public Map<String, Queue<BlockState>> getMapOfCages() {
        return this.mapOfCages;
    }

    public Map<String, Queue<BlockState>> getMapOfRestoreCages() {
        return this.mapOfRestoreCages;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
