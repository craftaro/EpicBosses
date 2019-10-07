package com.songoda.epicbosses.skills.custom.cage;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class CageLocationData {

    private final Location location;
    private BlockState oldBlockState;
    private int amountOfCages = 0;

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

    public void setOldBlockState(BlockState oldBlockState) {
        this.oldBlockState = oldBlockState;
    }

    public int getAmountOfCages() {
        return this.amountOfCages;
    }

    public void setAmountOfCages(int amountOfCages) {
        this.amountOfCages = amountOfCages;
    }

    public Location getLocation() {
        return this.location;
    }
}
