package com.songoda.epicbosses.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public interface IFactionHelper {

    boolean isFriendly(Player a, Player b);

    boolean isFriendly(Player a, Location location);

    boolean isInWarzone(Location location);

    boolean isInClaimedLand(Location location);
}
