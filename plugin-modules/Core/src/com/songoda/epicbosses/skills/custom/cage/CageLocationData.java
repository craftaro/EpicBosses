package com.songoda.epicbosses.skills.custom.cage;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class CageLocationData {

    private BlockState oldBlockState;
    private int amountOfCages = 0;

    private final Location location;

    public CageLocationData(Location location, int amountOfCages) {
        this(location);

        this.amountOfCages = amountOfCages;
    }

    public CageLocationData(Location location) {
        this.location = location;
    }

    public BlockState getOldBlockState() {
        return this.oldBlockState;
    }

    public int getAmountOfCages() {
        return this.amountOfCages;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setOldBlockState(BlockState oldBlockState) {
        this.oldBlockState = oldBlockState;
    }

    public void setAmountOfCages(int amountOfCages) {
        this.amountOfCages = amountOfCages;
    }
}
