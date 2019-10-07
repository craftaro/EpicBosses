package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.handlers.BossDisplayNameHandler;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
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

    public StatisticMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
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
        PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();

        if(health == null) health = 0.0;
        if(displayName == null) displayName = "N/A";

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{health}", NumberUtils.get().formatDouble(health));
        replaceMap.put("{displayName}", displayName);
        panelBuilder.addReplaceData(replaceMap);

        counter.addSlotCounter("DisplayName")
                .addSlotCounter("EntityType")
                .addSlotCounter("Health");

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getStatisticListEditMenu(), bossEntity);

        fillPanel(panel, bossEntity, entityStatsElement);

        ServerUtils.get().runTaskAsync(() -> {
            counter.getSlotsWith("DisplayName").forEach(slot -> panel.setOnClick(slot, getDisplayNameAction(bossEntity, entityStatsElement)));
            counter.getSlotsWith("EntityType").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getEntityTypeEditMenu().openFor((Player) event.getWhoClicked(), bossEntity, entityStatsElement)));
            counter.getSlotsWith("Health").forEach(slot -> panel.setOnClick(slot, getHealthAction(bossEntity, entityStatsElement)));
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getDisplayNameAction(BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        return event -> {
            if(!bossEntity.isEditing()) {
                Message.Boss_Edit_CannotBeModified.msg(event.getWhoClicked());
                return;
            }

            Player humanEntity = (Player) event.getWhoClicked();
            BossDisplayNameHandler bossDisplayNameHandler = new BossDisplayNameHandler(humanEntity, bossEntity, entityStatsElement, this.bossesFileManager, this);

            Message.Boss_Statistics_SetDisplayName.msg(humanEntity);
            bossDisplayNameHandler.handle();
            humanEntity.closeInventory();
        };
    }

    private ClickAction getHealthAction(BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        return event -> {
            if(!bossEntity.isEditing()) {
                Message.Boss_Edit_CannotBeModified.msg(event.getWhoClicked());
                return;
            }

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

            double newHealth = currentChance + healthToModifyBy;

            if(newHealth < 0.0) {
                newHealth = 1.0;
            }

            entityStatsElement.getMainStats().setHealth(newHealth);
            this.bossesFileManager.save();
            Message.Boss_Statistics_SetChance.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newHealth));
            openFor((Player) event.getWhoClicked(), bossEntity, entityStatsElement);
        };
    }
}
