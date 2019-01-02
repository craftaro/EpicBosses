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
    @Expose @Getter @Setter private Boolean spawnWhenCheckIsntLoaded, overrideDefaultSpawnMessage;
    @Expose @Getter @Setter private String spawnMessage;

    public AutoSpawnSettings(int maxAliveAtOnce, int amountPerSpawn, boolean spawnWhenCheckIsntLoaded) {
        this.maxAliveAtOnce = maxAliveAtOnce;
        this.amountPerSpawn = amountPerSpawn;
        this.spawnWhenCheckIsntLoaded = spawnWhenCheckIsntLoaded;
    }

}
