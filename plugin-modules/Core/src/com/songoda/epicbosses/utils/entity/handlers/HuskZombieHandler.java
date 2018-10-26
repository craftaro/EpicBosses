package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Jun-18
 */
public class HuskZombieHandler implements ICustomEntityHandler {

    private VersionHandler versionHandler = new VersionHandler();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(this.versionHandler.getVersion().isHigherThanOrEqualTo(Versions.v1_10_R1)) {
            return (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.HUSK);
        }

        throw new NullPointerException("This feature is only implemented in version 1.10 and above of Minecraft.");
    }
}
