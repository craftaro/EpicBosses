package net.aminecraftdev.custombosses.innerapi.dependencies;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public interface FactionBuilder {

    /** Used to tell if 2 players are
     * 	friendly.
     *
     * @param a - The first player.
     * @param b - The second player.
     *
     */

    boolean isFriendly(Player a, Player b);


    /** Used to tell if a player is in friendly
     *  land, in other words to tell if they are
     *  in ally, truce or their own land. If not
     *  returns false.
     *
     *  @param a - The player.
     *  @param location - The location we are checking.
     *
     */

    boolean isFriendly(Player a, Location location);


    /** Used to check if a location is in the warzone.
     *  I used this in my CustomBosses plugin to check
     *  if the player was inside a warzone when spawning
     *  the boss.
     *
     *  @param location - The location we are checking.
     *
     */

    boolean isInWarzone(Location location);


    /** Used to check if the location is claimed by a faction
     *  other then Wilderness, Warzone or Safezone. If it is
     *  another faction it will return true, otherwise if its
     *  in Wilderness, Warzone or Safezone it will return false.
     *
     *  @param location - The location that is being tested.
     *
     */
    boolean isInClaimedLand(Location location);

}
