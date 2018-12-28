package com.songoda.epicbosses.panel.droptables.types.spray;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Dec-18
 */
public class SprayRewardMainEditorPanel extends SubSubVariablePanelHandler<DropTable, SprayTableElement, String> {

    private DropTableFileManager dropTableFileManager;

    public SprayRewardMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.dropTableFileManager = plugin.getDropTableFileManager();
    }

    @Override
    public void openFor(Player player, DropTable dropTable, SprayTableElement sprayTableElement, String string) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();
        Double chance = sprayTableElement.getSprayRewards().get(string);

        if(chance == null) chance = 50.0;

        replaceMap.put("{chance}", NumberUtils.get().formatDouble(chance));
        replaceMap.put("{itemStack}", string);
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getSprayRewardListEditMenu(), dropTable, sprayTableElement);

        panelBuilderCounter.getSlotsWith("Chance").forEach(slot -> panel.setOnClick(slot, getChanceAction(dropTable, sprayTableElement, string)));
        panelBuilderCounter.getSlotsWith("Remove").forEach(slot -> panel.setOnClick(slot, getRemoveAction(dropTable, sprayTableElement, string)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getChanceAction(DropTable dropTable, SprayTableElement sprayTableElement, String name) {
        return event -> {
            ClickType clickType = event.getClick();
            double amountToModifyBy;

            if(clickType == ClickType.SHIFT_LEFT) {
                amountToModifyBy = 0.1;
            } else if(clickType == ClickType.RIGHT) {
                amountToModifyBy = -1.0;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                amountToModifyBy = -0.1;
            } else {
                amountToModifyBy = 1.0;
            }

            String modifyValue = amountToModifyBy > 0? "increased" : "decreased";
            Map<String, Double> rewards = sprayTableElement.getSprayRewards();
            double currentValue = rewards.getOrDefault(name, 50.0);
            double newValue = currentValue + amountToModifyBy;

            if(newValue < 0) {
                newValue = 0;
            }

            if(newValue > 100) {
                newValue = 100;
            }

            rewards.put(name, newValue);
            sprayTableElement.setSprayRewards(rewards);
            save(dropTable, sprayTableElement);
            openFor((Player) event.getWhoClicked(), dropTable, sprayTableElement, name);

            Message.Boss_DropTable_SprayRewardChance.msg(event.getWhoClicked(), modifyValue, BossAPI.getDropTableName(dropTable), NumberUtils.get().formatDouble(newValue));
        };
    }

    private ClickAction getRemoveAction(DropTable dropTable, SprayTableElement sprayTableElement, String name) {
        return event -> {
            Map<String, Double> current = sprayTableElement.getSprayRewards();

            current.remove(name);
            sprayTableElement.setSprayRewards(current);
            save(dropTable, sprayTableElement);

            Message.Boss_DropTable_SprayRewardRemoved.msg(event.getWhoClicked());
            this.bossPanelManager.getSprayRewardListEditMenu().openFor((Player) event.getWhoClicked(), dropTable, sprayTableElement);
        };
    }

    private void save(DropTable dropTable, SprayTableElement sprayTableElement) {
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(sprayTableElement));
        this.dropTableFileManager.save();
    }
}
