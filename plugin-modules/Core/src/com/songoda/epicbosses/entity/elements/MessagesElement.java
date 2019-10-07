package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class MessagesElement {

    @Expose
    private OnSpawnMessageElement onSpawn;
    @Expose
    private OnDeathMessageElement onDeath;
    @Expose
    private TauntElement taunts;

    public MessagesElement(OnSpawnMessageElement onSpawn, OnDeathMessageElement onDeath, TauntElement tauntElement) {
        this.onDeath = onDeath;
        this.onSpawn = onSpawn;
        this.taunts = tauntElement;
    }

    public OnSpawnMessageElement getOnSpawn() {
        return this.onSpawn;
    }

    public OnDeathMessageElement getOnDeath() {
        return this.onDeath;
    }

    public TauntElement getTaunts() {
        return this.taunts;
    }

    public void setOnSpawn(OnSpawnMessageElement onSpawn) {
        this.onSpawn = onSpawn;
    }

    public void setOnDeath(OnDeathMessageElement onDeath) {
        this.onDeath = onDeath;
    }

    public void setTaunts(TauntElement taunts) {
        this.taunts = taunts;
    }
}
