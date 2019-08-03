package com.songoda.epicbosses.utils;

import org.bukkit.Location;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public interface IWorldGuardHelper {

    boolean isPvpAllowed(Location location);

    boolean isBreakAllowed(Location location);

    boolean isExplosionsAllowed(Location location);

    List<String> getRegionNames(Location location);

    boolean isMobSpawningAllowed(Location location);

}
