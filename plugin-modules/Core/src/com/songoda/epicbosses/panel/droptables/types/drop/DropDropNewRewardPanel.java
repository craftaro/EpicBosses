package com.songoda.epicbosses.panel.droptables.types.drop;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
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
public class DropDropNewRewardPanel extends DropTableNewRewardEditorPanel<DropTableElement> {

    public DropDropNewRewardPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public List<String> getCurrentKeys(DropTableElement dropTableElement) {
        return new ArrayList<>(dropTableElement.getDropRewards().keySet());
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
    public void saveDropTable(DropTableFileManager dropTableFileManager, DropTable dropTable, DropTableElement dropTableElement) {
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(dropTableElement));
        dropTableFileManager.save();
    }

    @Override
    public ISubSubVariablePanelHandler<DropTable, DropTableElement, String> getRewardMainEditMenu() {
        return this.bossPanelManager.getDropDropRewardMainEditPanel();
    }
}
