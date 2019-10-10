package com.songoda.epicbosses.holder;

import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.SpawnType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jan-19
 */
public class ActiveAutoSpawnHolder {

    private final SpawnType spawnType;
    private final AutoSpawn autoSpawn;

    private List<ActiveBossHolder> activeBossHolders = new ArrayList<>();

    public ActiveAutoSpawnHolder(SpawnType spawnType, AutoSpawn autoSpawn) {
        this.autoSpawn = autoSpawn;
        this.spawnType = spawnType;
    }

    public int getCurrentActiveBossHolders() {
        return this.activeBossHolders.size();
    }

    protected boolean canSpawn() {
        return true;
    }

    public void clear() {
        this.activeBossHolders.clear();
    }

    public SpawnType getSpawnType() {
        return this.spawnType;
    }

    public AutoSpawn getAutoSpawn() {
        return this.autoSpawn;
    }

    public List<ActiveBossHolder> getActiveBossHolders() {
        return this.activeBossHolders;
    }
}
