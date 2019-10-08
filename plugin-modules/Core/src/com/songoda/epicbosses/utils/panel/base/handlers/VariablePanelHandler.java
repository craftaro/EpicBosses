package com.songoda.epicbosses.utils.panel.base.handlers;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public abstract class VariablePanelHandler<Variable> extends BasePanelHandler implements IVariablePanelHandler<Variable> {

    public VariablePanelHandler(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }
}
