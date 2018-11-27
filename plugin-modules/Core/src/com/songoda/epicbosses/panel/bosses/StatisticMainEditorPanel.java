package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 26-Nov-18
 */
public class StatisticMainEditorPanel extends SubVariablePanelHandler<BossEntity, EntityStatsElement> {

    private BossesFileManager bossesFileManager;

    public StatisticMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = plugin.getBossesFileManager();
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity, EntityStatsElement entityStatsElement) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        String displayName = entityStatsElement.getMainStats().getDisplayName();
        Double health = entityStatsElement.getMainStats().getHealth();

        if(health == null) health = 0.0;
        if(displayName == null) displayName = "N/A";

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{health}", NumberUtils.get().formatDouble(health));
        replaceMap.put("{displayName}", displayName);
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getStatisticListEditMenu(), bossEntity);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        fillPanel(panel, bossEntity, entityStatsElement);

        counter.getSlotsWith("DisplayName").forEach(slot -> panel.setOnClick(slot, event -> {}));
        counter.getSlotsWith("EntityType").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getSkillListBossEditMenu().openFor((Player) event.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("Health").forEach(slot -> panel.setOnClick(slot, getHealthAction(bossEntity, entityStatsElement)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getHealthAction(BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        return event -> {
            ClickType clickType = event.getClick();
            double healthToModifyBy = 0.0;

            if(clickType == ClickType.LEFT) {
                healthToModifyBy = 1.0;
            } else if(clickType == ClickType.SHIFT_LEFT) {
                healthToModifyBy = 0.1;
            } else if(clickType == ClickType.RIGHT) {
                healthToModifyBy = -1.0;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                healthToModifyBy = -0.1;
            }

            String modifyValue = healthToModifyBy > 0.0? "increased" : "decreased";
            Double currentChance = entityStatsElement.getMainStats().getHealth();

            if(currentChance == null) currentChance = 0.0;

            double newChance = currentChance + healthToModifyBy;

            if(newChance < 0.0) {
                newChance = 1.0;
            }

            entityStatsElement.getMainStats().setHealth(newChance);
            this.bossesFileManager.save();
            Message.Boss_Skills_SetChance.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newChance));
            openFor((Player) event.getWhoClicked(), bossEntity, entityStatsElement);
        };
    }
}
