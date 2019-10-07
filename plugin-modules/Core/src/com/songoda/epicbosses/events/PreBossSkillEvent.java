package com.songoda.epicbosses.events;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 17-Nov-18
 */
public class PreBossSkillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private LivingEntity livingEntityDamaged, damagingEntity;
    private ActiveBossHolder activeBossHolder;

    public PreBossSkillEvent(ActiveBossHolder activeBossHolder, LivingEntity livingEntity, LivingEntity damagingEntity) {
        this.activeBossHolder = activeBossHolder;
        this.livingEntityDamaged = livingEntity;
        this.damagingEntity = damagingEntity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LivingEntity getLivingEntityDamaged() {
        return this.livingEntityDamaged;
    }

    public LivingEntity getDamagingEntity() {
        return this.damagingEntity;
    }

    public ActiveBossHolder getActiveBossHolder() {
        return this.activeBossHolder;
    }
}
