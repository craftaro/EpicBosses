package com.songoda.epicbosses.events;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 17-Nov-18
 */
public class PreBossSpawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private ActiveBossHolder activeBossHolder;

    public PreBossSpawnEvent(ActiveBossHolder activeBossHolder) {
        this.activeBossHolder = activeBossHolder;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public ActiveBossHolder getActiveBossHolder() {
        return this.activeBossHolder;
    }
}
