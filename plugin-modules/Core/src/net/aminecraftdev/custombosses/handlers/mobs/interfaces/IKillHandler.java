package net.aminecraftdev.custombosses.handlers.mobs.interfaces;

import org.bukkit.Location;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public interface IKillHandler {

    void killBoss(Location location);

    void killMessage(Location location);

}
