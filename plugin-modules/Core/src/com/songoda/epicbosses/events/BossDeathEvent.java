package com.songoda.epicbosses.events;

import lombok.Getter;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 21-Oct-18
 */
public class BossDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter private final ActiveBossHolder activeBossHolder;
    @Getter private final boolean autoSpawn;

    public BossDeathEvent(ActiveBossHolder activeBossHolder, boolean autoSpawn) {
        this.activeBossHolder = activeBossHolder;
        this.autoSpawn = autoSpawn;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
