package com.songoda.epicbosses.utils.panel.base;

import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public interface IPanelHandler {

    PanelBuilder getPanelBuilder();

    Panel getPanel();

    void initializePanel(PanelBuilder panelBuilder);

    void fillPanel(Panel panel);

    void openFor(Player player);

}
