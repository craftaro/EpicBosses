package com.songoda.epicbosses.utils.panel.base;

import com.songoda.epicbosses.utils.panel.Panel;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public interface IPanelHandler extends IBasicPanelHandler {

    void fillPanel(Panel panel);

    void openFor(Player player);

}
