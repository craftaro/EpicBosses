package com.songoda.epicbosses.utils;

import com.songoda.epicbosses.holder.ActiveBossHolder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public interface IMechanic<Entity> {

    boolean applyMechanic(Entity entity, ActiveBossHolder activeBossHolder);

}
