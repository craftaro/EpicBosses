package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
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
 * @since 23-Nov-18
 */
public class WeaponsEditorPanel extends SubVariablePanelHandler<BossEntity, EntityStatsElement> {

    public WeaponsEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
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
                .setParentPanelHandler(this.bossPanelManager.getWeaponListEditMenu(), bossEntity);
        PanelBuilderCounter panelBuilderCounter = panel.getPanelBuilderCounter();

        panelBuilderCounter.getSlotsWith("MainHand").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getMainHandEditorMenu().openFor(player, bossEntity, entityStatsElement)));
        panelBuilderCounter.getSlotsWith("OffHand").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getOffHandEditorMenu().openFor(player, bossEntity, entityStatsElement)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("MainHand")
                .addSlotCounter("OffHand");
    }
}
