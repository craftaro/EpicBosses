package com.songoda.epicbosses.panel.droptables.types.drop;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
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
 * @since 29-Dec-18
 */
public class DropRewardMainEditorPanel extends SubSubVariablePanelHandler<DropTable, DropTableElement, String> {

    private DropTableFileManager dropTableFileManager;

    public DropRewardMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.dropTableFileManager = plugin.getDropTableFileManager();
    }

    @Override
    public void openFor(Player player, DropTable dropTable, DropTableElement dropTableElement, String s) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();
        Double chance = dropTableElement.getDropRewards().get(s);

        if(chance == null) chance = 50.0;

        replaceMap.put("{chance}", NumberUtils.get().formatDouble(chance));
        replaceMap.put("{itemStack}", s);
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getDropRewardListEditMenu(), dropTable, dropTableElement);

        panelBuilderCounter.getSlotsWith("Chance").forEach(slot -> panel.setOnClick(slot, getChanceAction(dropTable, dropTableElement, s)));
        panelBuilderCounter.getSlotsWith("Remove").forEach(slot -> panel.setOnClick(slot, getRemoveAction(dropTable, dropTableElement, s)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getChanceAction(DropTable dropTable, DropTableElement dropTableElement, String name) {
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
            Map<String, Double> rewards = dropTableElement.getDropRewards();
            double currentValue = rewards.getOrDefault(name, 50.0);
            double newValue = currentValue + amountToModifyBy;

            if(newValue < 0) {
                newValue = 0;
            }

            if(newValue > 100) {
                newValue = 100;
            }

            rewards.put(name, newValue);
            dropTableElement.setDropRewards(rewards);
            save(dropTable, dropTableElement);
            openFor((Player) event.getWhoClicked(), dropTable, dropTableElement, name);

            Message.Boss_DropTable_DropRewardChance.msg(event.getWhoClicked(), modifyValue, BossAPI.getDropTableName(dropTable), NumberUtils.get().formatDouble(newValue));
        };
    }

    private ClickAction getRemoveAction(DropTable dropTable, DropTableElement dropTableElement, String name) {
        return event -> {
            Map<String, Double> current = dropTableElement.getDropRewards();

            current.remove(name);
            dropTableElement.setDropRewards(current);
            save(dropTable, dropTableElement);

            Message.Boss_DropTable_DropRewardRemoved.msg(event.getWhoClicked());
            this.bossPanelManager.getDropRewardListEditMenu().openFor((Player) event.getWhoClicked(), dropTable, dropTableElement);
        };
    }

    private void save(DropTable dropTable, DropTableElement dropTableElement) {
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(dropTableElement));
        this.dropTableFileManager.save();
    }
}
