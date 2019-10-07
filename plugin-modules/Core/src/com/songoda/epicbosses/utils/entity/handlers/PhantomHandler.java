package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;

public class PhantomHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_13))
            throw new NullPointerException("This feature is only implemented in version 1.13 and above of Minecraft.");

        int size = 4;
        if (entityType.contains(":")) {
            String[] split = entityType.split(":");
            size = Integer.parseInt(split[1]);
        }

        Phantom phantom = (Phantom) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.PHANTOM);
        phantom.setSize(size);

        return phantom;
    }
}
