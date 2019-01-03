package com.songoda.epicbosses.autospawns.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.listeners.IBossDeathHandler;
import com.songoda.epicbosses.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.event.Listener;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class IntervalSpawnElement {

    @Expose @Getter @Setter private Boolean spawnAfterLastBossIsKilled;
    @Expose @Getter @Setter private String location, placeholder;
    @Expose @Getter @Setter private Integer spawnRate;

    public IntervalSpawnElement(String location, String placeholder, Integer spawnRate, boolean spawnAfterLastBossIsKilled) {
        this.location = location;
        this.placeholder = placeholder;
        this.spawnRate = spawnRate;
        this.spawnAfterLastBossIsKilled = spawnAfterLastBossIsKilled;
    }

    public boolean attemptSpawn(IBossDeathHandler bossDeathHandler) {

        return true;
    }

    public Location getSpawnLocation() {
        return StringUtils.get().fromStringToLocation(this.location);
    }

}
