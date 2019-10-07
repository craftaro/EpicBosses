package com.songoda.epicbosses.events;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 20-Oct-18
 */
public class BossDamageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private ActiveBossHolder activeBossHolder;
    private LivingEntity livingEntity;
    private Location damageLocation;
    private double damage;

    public BossDamageEvent(ActiveBossHolder activeBossHolder, LivingEntity livingEntity, Location damageLocation, double damageAmount) {
        this.activeBossHolder = activeBossHolder;
        this.damageLocation = damageLocation;
        this.livingEntity = livingEntity;
        this.damage = damageAmount;
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

    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    public Location getDamageLocation() {
        return this.damageLocation;
    }

    public double getDamage() {
        return this.damage;
    }
}
