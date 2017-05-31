package net.aminecraftdev.custombosses.handlers.mobs.interfaces;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public interface IMobHandler {

    ConfigurationSection getConfigurationSection();

    double getMaxHealth();

    double getCurrentHealth();

    UUID getUniqueId();

    boolean isAutoBoss();

    void spawn(Location location);

    void kill(Location location);

}
