package com.songoda.epicbosses.utils.panel.base;

import com.songoda.epicbosses.utils.panel.Panel;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public interface IVariablePanelHandler<Variable> extends IBasicPanelHandler {

    void fillPanel(Panel panel, Variable variable);

    void openFor(Player player, Variable variable);

}
