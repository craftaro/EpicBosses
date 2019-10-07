package com.songoda.epicbosses.events;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.interfaces.ISkillHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 17-Nov-18
 */
public class BossSkillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private ActiveBossHolder activeBossHolder;
    private ISkillHandler skillHandler;
    private Skill skill;

    public BossSkillEvent(ActiveBossHolder activeBossHolder, ISkillHandler skillHandler, Skill skill) {
        this.activeBossHolder = activeBossHolder;
        this.skillHandler = skillHandler;
        this.skill = skill;
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

    public ISkillHandler getSkillHandler() {
        return this.skillHandler;
    }

    public Skill getSkill() {
        return this.skill;
    }
}
