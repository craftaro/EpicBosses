package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class ZombieBabyHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        Zombie zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
        zombie.setBaby(true);

        return zombie;
    }
}
