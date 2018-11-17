package com.songoda.epicbosses.events;

import lombok.Getter;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class PreBossSpawnItemEvent extends PreBossSpawnEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter private ItemStack itemStackUsed;
    @Getter private Player player;

    public PreBossSpawnItemEvent(ActiveBossHolder activeBossHolder, Player player, ItemStack itemStackUsed) {
        super(activeBossHolder);

        this.itemStackUsed = itemStackUsed;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
