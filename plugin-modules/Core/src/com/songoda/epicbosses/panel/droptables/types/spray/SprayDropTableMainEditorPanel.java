package com.songoda.epicbosses.panel.droptables.types.spray;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Dec-18
 */
public class SprayDropTableMainEditorPanel extends SubVariablePanelHandler<DropTable, SprayTableElement> {

    public SprayDropTableMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, SprayTableElement sprayTableElement) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable, SprayTableElement sprayTableElement) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        replaceMap.put("{randomDrops}", StringUtils.get().formatString(""+sprayTableElement.getRandomSprayDrops()));
        replaceMap.put("{maxDrops}", NumberUtils.get().formatDouble(sprayTableElement.getSprayMaxDrops()));
        replaceMap.put("{maxDistance}", NumberUtils.get().formatDouble(sprayTableElement.getSprayMaxDistance()));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getDropTables());

        panelBuilderCounter.getSlotsWith("Rewards").forEach(slot -> panel.setOnClick(slot, event -> {}));
        panelBuilderCounter.getSlotsWith("RandomDrops").forEach(slot -> panel.setOnClick(slot, event -> {}));
        panelBuilderCounter.getSlotsWith("MaxDistance").forEach(slot -> panel.setOnClick(slot, event -> {}));
        panelBuilderCounter.getSlotsWith("MaxDrops").forEach(slot -> panel.setOnClick(slot, event -> {}));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
