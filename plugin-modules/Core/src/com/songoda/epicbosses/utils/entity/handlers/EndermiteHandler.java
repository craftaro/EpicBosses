package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jul-18
 */
public class EndermiteHandler implements ICustomEntityHandler {

    private VersionHandler versionHandler = new VersionHandler();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(ServerVersion.isServerVersionBelow(ServerVersion.V1_8))
            throw new NullPointerException("This feature is only implemented in version 1.8 and above of Minecraft.");

        return (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ENDERMITE);
    }
}
