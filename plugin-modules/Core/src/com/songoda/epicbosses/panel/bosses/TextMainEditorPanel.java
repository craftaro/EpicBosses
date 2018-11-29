package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Nov-18
 */
public class TextMainEditorPanel extends VariablePanelHandler<BossEntity> {

    public TextMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getMainBossEditMenu(), bossEntity);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        counter.getSlotsWith("OnSpawn").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getOnSpawnSubTextEditMenu().openFor((Player) event.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("OnDeath").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getOnDeathSubTextEditMenu().openFor((Player) event.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("Taunts").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getMainTauntEditMenu().openFor((Player) event.getWhoClicked(), bossEntity)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("OnSpawn")
                .addSlotCounter("OnDeath")
                .addSlotCounter("Taunts");
    }
}
