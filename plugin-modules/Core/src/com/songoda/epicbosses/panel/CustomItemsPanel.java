package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.base.PanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 *
 * TODO
 */
public class CustomItemsPanel extends PanelHandler {

    public CustomItemsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);

        fillPanel();
    }

    @Override
    public void fillPanel() {
        this.panel.setParentPanel(this.bossPanelManager.getMainMenu().getPanel());
    }
}
