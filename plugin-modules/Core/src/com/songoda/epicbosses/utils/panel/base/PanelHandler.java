package com.songoda.epicbosses.utils.panel.base;

import lombok.Getter;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public abstract class PanelHandler implements IPanelHandler {

    protected final BossPanelManager bossPanelManager;

    @Getter private PanelBuilder panelBuilder;
    @Getter protected Panel panel = null;

    public PanelHandler(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        this.bossPanelManager = bossPanelManager;
        this.panelBuilder = panelBuilder;

        initializePanel(panelBuilder);
    }
}
