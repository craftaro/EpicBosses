package com.songoda.epicbosses.autospawns.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.autospawns.IAutoSpawnElement;
import com.songoda.epicbosses.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class IntervalSpawnElement implements IAutoSpawnElement {

    @Expose @Getter @Setter private String location, placeholder;
    @Expose @Getter @Setter private Integer spawnRate;

    public IntervalSpawnElement(String location, String placeholder, Integer spawnRate) {
        this.location = location;
        this.placeholder = placeholder;
        this.spawnRate = spawnRate;
    }

    @Override
    public void attemptSpawn() {

    }

    public Location getSpawnLocation() {
        return StringUtils.get().fromStringToLocation(this.location);
    }

}
