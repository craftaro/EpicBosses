package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.ServerUtils;
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
 * @since 19-Nov-18
 */
public class EquipmentEditorPanel extends SubVariablePanelHandler<BossEntity, EntityStatsElement> {

    public EquipmentEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity, EntityStatsElement entityStatsElement) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        ServerUtils.get().runTaskAsync(() -> {
            Map<String, String> replaceMap = new HashMap<>();

            replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));

            PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

            panelBuilder.addReplaceData(replaceMap);

            Panel panel = panelBuilder.getPanel()
                    .setDestroyWhenDone(true)
                    .setCancelLowerClick(true)
                    .setCancelClick(true)
                    .setParentPanelHandler(this.bossPanelManager.getEquipmentListEditMenu(), bossEntity);
            PanelBuilderCounter panelBuilderCounter = panel.getPanelBuilderCounter();

            panelBuilderCounter.getSlotsWith("Helmet").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getHelmetEditorMenu().openFor(player, bossEntity, entityStatsElement)));
            panelBuilderCounter.getSlotsWith("Chestplate").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getChestplateEditorMenu().openFor(player, bossEntity, entityStatsElement)));
            panelBuilderCounter.getSlotsWith("Leggings").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getLeggingsEditorMenu().openFor(player, bossEntity, entityStatsElement)));
            panelBuilderCounter.getSlotsWith("Boots").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getBootsEditorMenu().openFor(player, bossEntity, entityStatsElement)));

            panel.openFor(player);
        });
    }
}
