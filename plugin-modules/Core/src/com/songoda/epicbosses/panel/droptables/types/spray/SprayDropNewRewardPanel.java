package com.songoda.epicbosses.panel.droptables.types.spray;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.panel.droptables.rewards.DropTableNewRewardEditorPanel;
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
public class SprayDropNewRewardPanel extends DropTableNewRewardEditorPanel<SprayTableElement> {

    public SprayDropNewRewardPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public List<String> getCurrentKeys(SprayTableElement sprayTableElement) {
        return new ArrayList<>(sprayTableElement.getSprayRewards().keySet());
    }

    @Override
    public ISubVariablePanelHandler<DropTable, SprayTableElement> getParentPanelHandler() {
        return this.bossPanelManager.getSprayDropRewardListPanel();
    }

    @Override
    public Map<String, Double> getRewards(SprayTableElement sprayTableElement) {
        return sprayTableElement.getSprayRewards();
    }

    @Override
    public void saveDropTable(DropTableFileManager dropTableFileManager, DropTable dropTable, SprayTableElement sprayTableElement) {
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(sprayTableElement));
        dropTableFileManager.save();
    }

    @Override
    public ISubSubVariablePanelHandler<DropTable, SprayTableElement, String> getRewardMainEditMenu() {
        return this.bossPanelManager.getSprayDropRewardMainEditPanel();
    }
}
