package com.songoda.epicbosses.panel.droptables.rewards.interfaces;

import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public interface IDropTableRewardMainEditor<SubVariable> {

    double getChance(SubVariable subVariable, String name);

    ISubVariablePanelHandler<DropTable, SubVariable> getParentPanelHandler();

    Map<String, Double> getRewards(SubVariable subVariable);

    void setRewards(SubVariable subVariable, Map<String, Double> rewards);

    void saveDropTable(DropTableFileManager dropTableFileManager, DropTable dropTable, SubVariable subVariable);

}
