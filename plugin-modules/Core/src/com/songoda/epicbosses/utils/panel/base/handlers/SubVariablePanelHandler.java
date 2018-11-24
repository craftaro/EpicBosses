package com.songoda.epicbosses.utils.panel.base.handlers;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.handlers.BasePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public abstract class SubVariablePanelHandler<Variable, SubVariable> extends BasePanelHandler implements ISubVariablePanelHandler<Variable, SubVariable> {

    public SubVariablePanelHandler(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    public SubVariablePanelHandler(BossPanelManager bossPanelManager, ConfigurationSection configurationSection) {
        super(bossPanelManager, configurationSection);
    }
}
