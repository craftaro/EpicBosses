package com.songoda.epicbosses.utils.panel.base;

import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Dec-18
 */
public interface ISubSubVariablePanelHandler<Variable, SubVariable, SubSubVariable> extends IBasicPanelHandler {

    void openFor(Player player, Variable variable, SubVariable subVariable, SubSubVariable subSubVariable);

}
