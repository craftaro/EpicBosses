package com.songoda.epicbosses.holder;

import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.SpawnType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jan-19
 */
public class ActiveAutoSpawnHolder {

    @Getter private final SpawnType spawnType;
    @Getter private final AutoSpawn autoSpawn;

    @Getter private List<ActiveBossHolder> activeBossHolders = new ArrayList<>();

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

}
