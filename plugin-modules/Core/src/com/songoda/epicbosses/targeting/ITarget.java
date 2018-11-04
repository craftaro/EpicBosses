package com.songoda.epicbosses.targeting;

import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Oct-18
 */
public interface ITarget {

    LivingEntity selectTarget(List<LivingEntity> nearbyEntities);

}
