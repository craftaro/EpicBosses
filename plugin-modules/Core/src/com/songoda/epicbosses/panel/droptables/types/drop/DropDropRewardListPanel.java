package com.songoda.epicbosses.panel.droptables.types.drop;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.droptables.rewards.DropTableRewardsListEditorPanel;
import com.songoda.epicbosses.utils.panel.base.ISubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class DropDropRewardListPanel extends DropTableRewardsListEditorPanel<DropTableElement> {

    public DropDropRewardListPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public Map<String, Double> getRewards(DropTableElement dropTableElement) {
        return dropTableElement.getDropRewards();
    }

    @Override
    public ISubVariablePanelHandler<DropTable, DropTableElement> getParentPanelHandler() {
        return this.bossPanelManager.getDropDropTableMainEditMenu();
    }

    @Override
    public ISubVariablePanelHandler<DropTable, DropTableElement> getNewRewardPanelHandler() {
        return this.bossPanelManager.getDropDropNewRewardEditPanel();
    }

    @Override
    public ISubSubVariablePanelHandler<DropTable, DropTableElement, String> getRewardMainEditPanel() {
        return this.bossPanelManager.getDropDropRewardMainEditPanel();
    }
}
