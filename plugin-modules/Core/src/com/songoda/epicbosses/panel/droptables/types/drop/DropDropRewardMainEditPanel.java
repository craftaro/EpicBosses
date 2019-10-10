package com.songoda.epicbosses.panel.droptables.types.drop;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.panel.droptables.rewards.DropTableRewardMainEditorPanel;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class DropDropRewardMainEditPanel extends DropTableRewardMainEditorPanel<DropTableElement> {

    public DropDropRewardMainEditPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public double getChance(DropTableElement dropTableElement, String name) {
        Map<String, Double> dropMap = dropTableElement.getDropRewards();

        return dropMap.getOrDefault(name, 50.0);
    }

    @Override
    public ISubVariablePanelHandler<DropTable, DropTableElement> getParentPanelHandler() {
        return this.bossPanelManager.getDropDropRewardListPanel();
    }

    @Override
    public Map<String, Double> getRewards(DropTableElement dropTableElement) {
        return dropTableElement.getDropRewards();
    }

    @Override
    public void setRewards(DropTableElement dropTableElement, Map<String, Double> rewards) {
        dropTableElement.setDropRewards(rewards);
    }

    @Override
    public void saveDropTable(DropTableFileManager dropTableFileManager, DropTable dropTable, DropTableElement dropTableElement) {
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(dropTableElement));
        dropTableFileManager.save();
    }
}
