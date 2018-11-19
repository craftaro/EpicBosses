package com.songoda.epicbosses.utils.panel.base;

import com.songoda.epicbosses.utils.panel.Panel;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public interface ISubVariablePanelHandler<Variable, SubVariable> extends IBasicPanelHandler {

    void fillPanel(Panel panel, Variable variable, SubVariable subVariable);

    void openFor(Player player, Variable variable, SubVariable subVariable);

}
