package com.songoda.epicbosses.panel.droptables.types.give.drops;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.panel.droptables.rewards.DropTableRewardMainEditorPanel;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class GiveDropRewardMainEditPanel extends DropTableRewardMainEditorPanel<GiveRewardEditHandler> {

    public GiveDropRewardMainEditPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public double getChance(GiveRewardEditHandler giveRewardEditHandler, String name) {
        Map<String, Double> dropMap = giveRewardEditHandler.getGiveTableSubElement().getItems();

        return dropMap.getOrDefault(name, 50.0);
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
    public void setRewards(GiveRewardEditHandler giveRewardEditHandler, Map<String, Double> rewards) {
        giveRewardEditHandler.getGiveTableSubElement().setItems(rewards);
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
}
