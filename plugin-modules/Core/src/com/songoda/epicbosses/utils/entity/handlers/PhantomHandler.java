package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;

public class PhantomHandler implements ICustomEntityHandler {

    private VersionHandler versionHandler = new VersionHandler();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(this.versionHandler.getVersion().isLessThan(Versions.v1_13_R1)) {
            throw new NullPointerException("This feature is only implemented in version 1.13 and above of Minecraft.");
        }

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
