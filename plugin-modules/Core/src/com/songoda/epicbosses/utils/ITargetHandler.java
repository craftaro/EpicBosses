package com.songoda.epicbosses.utils;

import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public interface ITargetHandler {

    boolean canTarget(LivingEntity livingEntity);

    void updateTarget();

    void createAutoTarget();

}
