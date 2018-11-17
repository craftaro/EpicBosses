package com.songoda.epicbosses.events;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 17-Nov-18
 */
public class BossSkillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter private ActiveBossHolder activeBossHolder;

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
