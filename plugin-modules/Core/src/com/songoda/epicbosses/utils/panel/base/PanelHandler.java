package com.songoda.epicbosses.utils.panel.base;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.base.handlers.BasePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public abstract class PanelHandler extends BasePanelHandler implements IPanelHandler {

    public PanelHandler(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }
}
