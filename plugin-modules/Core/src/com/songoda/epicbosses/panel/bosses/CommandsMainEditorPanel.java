package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.ServerUtils;
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
public class CommandsMainEditorPanel extends VariablePanelHandler<BossEntity> {

    public CommandsMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        ServerUtils.get().runTaskAsync(() -> {
            Map<String, String> replaceMap = new HashMap<>();
            PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

            replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
            replaceMap.put("{onSpawn}", bossEntity.getCommands().getOnSpawn());
            replaceMap.put("{onDeath}", bossEntity.getCommands().getOnDeath());
            panelBuilder.addReplaceData(replaceMap);

            PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();
            Panel panel = panelBuilder.getPanel()
                    .setDestroyWhenDone(true)
                    .setCancelClick(true)
                    .setCancelLowerClick(true)
                    .setParentPanelHandler(this.bossPanelManager.getMainBossEditMenu(), bossEntity);

            counter.getSlotsWith("OnSpawn").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getOnSpawnCommandEditMenu().openFor(player, bossEntity)));
            counter.getSlotsWith("OnDeath").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getOnDeathCommandEditMenu().openFor(player, bossEntity)));

            panel.openFor(player);
        });
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
