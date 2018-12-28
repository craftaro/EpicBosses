package com.songoda.epicbosses.utils.panel.base.handlers;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.base.ISubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Dec-18
 */
public abstract class SubSubVariablePanelHandler<Variable, SubVariable, SubSubVariable> extends BasePanelHandler implements ISubSubVariablePanelHandler<Variable, SubVariable, SubSubVariable> {

    public SubSubVariablePanelHandler(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    public SubSubVariablePanelHandler(BossPanelManager bossPanelManager, ConfigurationSection configurationSection) {
        super(bossPanelManager, configurationSection);
    }

}
