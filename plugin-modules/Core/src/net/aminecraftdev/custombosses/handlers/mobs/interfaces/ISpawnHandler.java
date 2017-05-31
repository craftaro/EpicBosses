package net.aminecraftdev.custombosses.handlers.mobs.interfaces;

import org.bukkit.Location;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public interface ISpawnHandler {

    void spawnBoss(Location location);

    void spawnMessage(Location location);

}
