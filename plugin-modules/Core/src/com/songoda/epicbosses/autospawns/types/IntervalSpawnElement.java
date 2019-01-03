package com.songoda.epicbosses.autospawns.types;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class IntervalSpawnElement {

    @Expose @Getter @Setter private String location, placeholder;
    @Expose @Getter @Setter private Integer spawnRate;

    public IntervalSpawnElement(String location, String placeholder, Integer spawnRate) {
        this.location = location;
        this.placeholder = placeholder;
        this.spawnRate = spawnRate;
    }

    public void attemptSpawn() {

    }

}
