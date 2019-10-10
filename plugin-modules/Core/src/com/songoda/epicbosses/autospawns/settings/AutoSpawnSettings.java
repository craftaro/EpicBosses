package com.songoda.epicbosses.autospawns.settings;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class AutoSpawnSettings {

    @Expose
    private Integer maxAliveAtOnce, amountPerSpawn;
    @Expose
    private Boolean spawnWhenChunkIsntLoaded, overrideDefaultSpawnMessage, shuffleEntitiesList;
    @Expose
    private String spawnMessage;

    public AutoSpawnSettings(int maxAliveAtOnce, int amountPerSpawn, boolean spawnWhenChunkIsntLoaded, boolean shuffleEntitiesList) {
        this.maxAliveAtOnce = maxAliveAtOnce;
        this.amountPerSpawn = amountPerSpawn;
        this.spawnWhenChunkIsntLoaded = spawnWhenChunkIsntLoaded;
        this.shuffleEntitiesList = shuffleEntitiesList;
    }

    public Integer getMaxAliveAtOnce() {
        return this.maxAliveAtOnce;
    }

    public void setMaxAliveAtOnce(Integer maxAliveAtOnce) {
        this.maxAliveAtOnce = maxAliveAtOnce;
    }

    public Integer getAmountPerSpawn() {
        return this.amountPerSpawn;
    }

    public void setAmountPerSpawn(Integer amountPerSpawn) {
        this.amountPerSpawn = amountPerSpawn;
    }

    public Boolean getSpawnWhenChunkIsntLoaded() {
        return this.spawnWhenChunkIsntLoaded;
    }

    public void setSpawnWhenChunkIsntLoaded(Boolean spawnWhenChunkIsntLoaded) {
        this.spawnWhenChunkIsntLoaded = spawnWhenChunkIsntLoaded;
    }

    public Boolean getOverrideDefaultSpawnMessage() {
        return this.overrideDefaultSpawnMessage;
    }

    public void setOverrideDefaultSpawnMessage(Boolean overrideDefaultSpawnMessage) {
        this.overrideDefaultSpawnMessage = overrideDefaultSpawnMessage;
    }

    public Boolean getShuffleEntitiesList() {
        return this.shuffleEntitiesList;
    }

    public void setShuffleEntitiesList(Boolean shuffleEntitiesList) {
        this.shuffleEntitiesList = shuffleEntitiesList;
    }

    public String getSpawnMessage() {
        return this.spawnMessage;
    }

    public void setSpawnMessage(String spawnMessage) {
        this.spawnMessage = spawnMessage;
    }
}
