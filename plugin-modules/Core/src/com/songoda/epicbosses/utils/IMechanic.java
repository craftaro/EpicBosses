package com.songoda.epicbosses.utils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public interface IMechanic<Entity, Holder> {

    boolean applyMechanic(Entity entity, Holder activeBossHolder);

}
