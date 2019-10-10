package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class CommandsElement {

    @Expose
    private String onSpawn, onDeath;

    public CommandsElement(String onSpawn, String onDeath) {
        this.onDeath = onDeath;
        this.onSpawn = onSpawn;
    }

    public String getOnSpawn() {
        return this.onSpawn;
    }

    public void setOnSpawn(String onSpawn) {
        this.onSpawn = onSpawn;
    }

    public String getOnDeath() {
        return this.onDeath;
    }

    public void setOnDeath(String onDeath) {
        this.onDeath = onDeath;
    }
}
