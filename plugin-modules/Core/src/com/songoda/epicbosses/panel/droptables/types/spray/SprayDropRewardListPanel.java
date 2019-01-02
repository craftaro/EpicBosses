package com.songoda.epicbosses.panel.droptables.types.spray;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
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
public class SprayDropRewardListPanel extends DropTableRewardsListEditorPanel<SprayTableElement> {

    public SprayDropRewardListPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public Map<String, Double> getRewards(SprayTableElement sprayTableElement) {
        return sprayTableElement.getSprayRewards();
    }

    @Override
    public ISubVariablePanelHandler<DropTable, SprayTableElement> getParentPanelHandler() {
        return this.bossPanelManager.getSprayDropTableMainEditMenu();
    }

    @Override
    public ISubVariablePanelHandler<DropTable, SprayTableElement> getNewRewardPanelHandler() {
        return this.bossPanelManager.getSprayDropNewRewardEditPanel();
    }

    @Override
    public ISubSubVariablePanelHandler<DropTable, SprayTableElement, String> getRewardMainEditPanel() {
        return this.bossPanelManager.getSprayDropRewardMainEditPanel();
    }
}
