package com.songoda.epicbosses.panel.handlers;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.PanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public abstract class MainListPanelHandler extends PanelHandler {

    public MainListPanelHandler(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    @Override
    public void openFor(Player player) {
        Panel panel = getPanelBuilder().getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanel(this.bossPanelManager.getMainMenu().getPanel());

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel));
        panel.openFor(player);
    }
}
