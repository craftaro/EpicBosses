package com.songoda.epicbosses.events;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 21-Oct-18
 */
public class PreBossDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private ActiveBossHolder activeBossHolder;
    private Location location;
    private Player killer;

    public PreBossDeathEvent(ActiveBossHolder activeBossHolder, Location location, Player killer) {
        this.activeBossHolder = activeBossHolder;
        this.location = location;
        this.killer = killer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ActiveBossHolder getActiveBossHolder() {
        return this.activeBossHolder;
    }

    public Location getLocation() {
        return this.location;
    }

    public Player getKiller() {
        return this.killer;
    }
}
