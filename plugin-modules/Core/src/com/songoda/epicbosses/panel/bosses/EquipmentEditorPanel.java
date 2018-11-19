package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public class EquipmentEditorPanel extends SubVariablePanelHandler<BossEntity, EntityStatsElement> {

    public EquipmentEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("Helmet")
                .addSlotCounter("Chestplate")
                .addSlotCounter("Leggings")
                .addSlotCounter("Boots");
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity, EntityStatsElement entityStatsElement) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));

        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelLowerClick(true)
                .setCancelClick(true)
                .setParentPanelHandler(this.bossPanelManager.getEquipmentListEditMenu(), bossEntity);



        panel.openFor(player);
    }
}
