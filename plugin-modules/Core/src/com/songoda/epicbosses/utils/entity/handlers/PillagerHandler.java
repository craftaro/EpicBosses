package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class PillagerHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_14))
            throw new NullPointerException("This feature is only implemented in version 1.14 and above of Minecraft.");

        return (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.PILLAGER);
    }
}
