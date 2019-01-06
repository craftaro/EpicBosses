package com.songoda.epicbosses.autospawns.settings;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class AutoSpawnSettings {

    @Expose @Getter @Setter private Integer maxAliveAtOnce, amountPerSpawn;
    @Expose @Getter @Setter private Boolean spawnWhenChunkIsntLoaded, overrideDefaultSpawnMessage, shuffleEntitiesList;
    @Expose @Getter @Setter private String spawnMessage;

    public AutoSpawnSettings(int maxAliveAtOnce, int amountPerSpawn, boolean spawnWhenChunkIsntLoaded, boolean shuffleEntitiesList) {
        this.maxAliveAtOnce = maxAliveAtOnce;
        this.amountPerSpawn = amountPerSpawn;
        this.spawnWhenChunkIsntLoaded = spawnWhenChunkIsntLoaded;
        this.shuffleEntitiesList = shuffleEntitiesList;
    }

}
