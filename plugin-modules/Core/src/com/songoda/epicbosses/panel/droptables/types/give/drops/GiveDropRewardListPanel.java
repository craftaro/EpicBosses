package com.songoda.epicbosses.panel.droptables.types.give.drops;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.droptables.rewards.DropTableRewardsListEditorPanel;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.panel.base.ISubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class GiveDropRewardListPanel extends DropTableRewardsListEditorPanel<GiveRewardEditHandler> {

    public GiveDropRewardListPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public Map<String, Double> getRewards(GiveRewardEditHandler giveRewardEditHandler) {
        return giveRewardEditHandler.getGiveTableSubElement().getItems();
    }

    @Override
    public ISubVariablePanelHandler<DropTable, GiveRewardEditHandler> getParentPanelHandler() {
        return this.bossPanelManager.getGiveRewardMainEditMenu();
    }

    @Override
    public ISubVariablePanelHandler<DropTable, GiveRewardEditHandler> getNewRewardPanelHandler() {
        return this.bossPanelManager.getGiveDropNewRewardEditPanel();
    }

    @Override
    public ISubSubVariablePanelHandler<DropTable, GiveRewardEditHandler, String> getRewardMainEditPanel() {
        return this.bossPanelManager.getGiveDropRewardMainEditPanel();
    }
}
