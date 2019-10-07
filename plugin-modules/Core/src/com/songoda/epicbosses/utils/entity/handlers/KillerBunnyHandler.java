package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class KillerBunnyHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_8))
            throw new NullPointerException("This feature is only implemented in version 1.8 and above of Minecraft.");

        Rabbit rabbit = (Rabbit) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.RABBIT);
        rabbit.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);

        return rabbit;
    }
}
