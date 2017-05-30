package net.aminecraftdev.custombosses.innerapi.inventory.base;

import org.bukkit.entity.Player;

/**
 * @author Debugged
 * @version 1.0
 * @since 13-5-2017
 */
@FunctionalInterface
public interface PanelCloseAction {

    /**
     * Called whenever a player closes the {@link net.aminecraftdev.custombosses.innerapi.inventory.Panel}
     * that this PanelCloseAction is associated with
     *
     * @param player Player
     */
    void onClose(Player player);

}
