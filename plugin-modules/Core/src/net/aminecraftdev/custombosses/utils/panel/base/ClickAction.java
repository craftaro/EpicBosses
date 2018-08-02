package net.aminecraftdev.custombosses.utils.panel.base;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Debugged
 * @version 1.0
 * @since 13-5-2017
 */
@FunctionalInterface
public interface ClickAction {

    /**
     * Called whenever the item that this
     * ClickAction is associated with is triggered
     *
     * @param e InventoryClickEvent
     */
    void onClick(InventoryClickEvent e);

}
