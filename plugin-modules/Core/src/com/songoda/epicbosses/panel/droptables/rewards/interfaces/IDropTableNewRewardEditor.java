package com.songoda.epicbosses.panel.droptables.rewards.interfaces;

import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.utils.panel.base.ISubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;

import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public interface IDropTableNewRewardEditor<SubVariable> {

    List<String> getCurrentKeys(SubVariable subVariable);

    ISubVariablePanelHandler<DropTable, SubVariable> getParentPanelHandler();

    Map<String, Double> getRewards(SubVariable subVariable);

    void saveDropTable(DropTableFileManager dropTableFileManager, DropTable dropTable, SubVariable subVariable);

    ISubSubVariablePanelHandler<DropTable, SubVariable, String> getRewardMainEditMenu();

}
