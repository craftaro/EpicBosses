package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class StraySkeletonHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(ServerVersion.isServerVersionBelow(ServerVersion.V1_10))
            throw new NullPointerException("This feature is only implemented in version 1.10 and above of Minecraft.");

        if(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_10))
            return (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.STRAY);

        Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
        skeleton.setSkeletonType(Skeleton.SkeletonType.STRAY);

        return skeleton;
    }
}
