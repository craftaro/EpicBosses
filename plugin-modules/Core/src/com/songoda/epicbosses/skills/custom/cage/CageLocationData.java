package com.songoda.epicbosses.skills.custom.cage;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BlockState;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class CageLocationData {

    @Getter @Setter private BlockState oldBlockState;
    @Getter @Setter private int amountOfCages = 0;

    @Getter private final Location location;

    public CageLocationData(Location location, int amountOfCages) {
        this(location);

        this.amountOfCages = amountOfCages;
    }

    public CageLocationData(Location location) {
        this.location = location;
    }

}
