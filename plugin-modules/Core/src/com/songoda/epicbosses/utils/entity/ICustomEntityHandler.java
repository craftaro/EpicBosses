package com.songoda.epicbosses.utils.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public interface ICustomEntityHandler {

    LivingEntity getBaseEntity(String entityType, Location spawnLocation);

}
