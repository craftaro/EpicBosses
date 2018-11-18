package com.songoda.epicbosses.events;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.Skill;
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
    @Getter private ISkillHandler skillHandler;
    @Getter private Skill skill;

    public BossSkillEvent(ActiveBossHolder activeBossHolder, ISkillHandler skillHandler, Skill skill) {
        this.activeBossHolder = activeBossHolder;
        this.skillHandler = skillHandler;
        this.skill = skill;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
