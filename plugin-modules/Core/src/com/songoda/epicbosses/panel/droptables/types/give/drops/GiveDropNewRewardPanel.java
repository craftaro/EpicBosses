package com.songoda.epicbosses.panel.droptables.types.give.drops;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.panel.droptables.rewards.DropTableNewRewardEditorPanel;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.panel.base.ISubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class GiveDropNewRewardPanel extends DropTableNewRewardEditorPanel<GiveRewardEditHandler> {

    public GiveDropNewRewardPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public List<String> getCurrentKeys(GiveRewardEditHandler giveRewardEditHandler) {
        return new ArrayList<>(giveRewardEditHandler.getGiveTableSubElement().getItems().keySet());
    }

    @Override
    public ISubVariablePanelHandler<DropTable, GiveRewardEditHandler> getParentPanelHandler() {
        return this.bossPanelManager.getGiveDropRewardListPanel();
    }

    @Override
    public Map<String, Double> getRewards(GiveRewardEditHandler giveRewardEditHandler) {
        return giveRewardEditHandler.getGiveTableSubElement().getItems();
    }

    @Override
    public void saveDropTable(DropTableFileManager dropTableFileManager, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
        GiveTableElement giveTableElement = giveRewardEditHandler.getGiveTableElement();
        Map<String, Map<String, GiveTableSubElement>> positionMap = giveTableElement.getGiveRewards();
        Map<String, GiveTableSubElement> rewardMap = positionMap.get(giveRewardEditHandler.getDamagePosition());

        rewardMap.put(giveRewardEditHandler.getDropSection(), giveTableSubElement);
        positionMap.put(giveRewardEditHandler.getDamagePosition(), rewardMap);
        giveTableElement.setGiveRewards(positionMap);
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(giveTableElement));
        dropTableFileManager.save();
    }

    @Override
    public ISubSubVariablePanelHandler<DropTable, GiveRewardEditHandler, String> getRewardMainEditMenu() {
        return this.bossPanelManager.getGiveDropRewardMainEditPanel();
    }
}
