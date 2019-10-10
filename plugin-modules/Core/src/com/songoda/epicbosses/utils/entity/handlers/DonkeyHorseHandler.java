package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jul-18
 */
public class DonkeyHorseHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_11))
            throw new NullPointerException("This feature is only implemented in version 1.11 and above of Minecraft.");

        if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_11))
            return (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.DONKEY);

        Horse horse = (Horse) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.HORSE);
        horse.setVariant(Horse.Variant.DONKEY);

        return horse;
    }
}
