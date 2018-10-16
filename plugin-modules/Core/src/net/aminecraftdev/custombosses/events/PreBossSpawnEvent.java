package net.aminecraftdev.custombosses.events;

import lombok.Getter;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class PreBossSpawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter private ActiveBossHolder activeBossHolder;
    @Getter private ItemStack itemStackUsed;
    @Getter private Player player;

    public PreBossSpawnEvent(ActiveBossHolder activeBossHolder, Player player, ItemStack itemStackUsed) {
        this.activeBossHolder = activeBossHolder;
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
