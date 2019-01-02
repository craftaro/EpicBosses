package com.songoda.epicbosses.panel.droptables.rewards.interfaces;

import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.utils.panel.base.ISubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public interface IDropTableRewardsListEditor<SubVariable> {

    Map<String, Double> getRewards(SubVariable subVariable);

    ISubVariablePanelHandler<DropTable, SubVariable> getParentPanelHandler();

    ISubVariablePanelHandler<DropTable, SubVariable> getNewRewardPanelHandler();

    ISubSubVariablePanelHandler<DropTable, SubVariable, String> getRewardMainEditPanel();

}
