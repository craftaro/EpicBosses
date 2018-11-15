package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.PanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 *
 * TODO
 */
public class AutoSpawnsPanel extends PanelHandler {

    public AutoSpawnsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);

//        fillPanel();
    }

//    @Override
//    public void fillPanel() {
//        this.panel.setParentPanel(this.bossPanelManager.getMainMenu().getPanel());
//    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    @Override
    public void fillPanel(Panel panel) {

    }

    @Override
    public void openFor(Player player) {

    }
}
