package com.songoda.epicbosses.panel.droptables.types.give.commands;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.ServerUtils;
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
 * @since 02-Jan-19
 */
public class GiveCommandRewardMainEditPanel extends SubSubVariablePanelHandler<DropTable, GiveRewardEditHandler, String> {

    private DropTableFileManager dropTableFileManager;

    public GiveCommandRewardMainEditPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.dropTableFileManager = plugin.getDropTableFileManager();
    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler, String s) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, Double> rewardMap = giveRewardEditHandler.getGiveTableSubElement().getCommands();
        Map<String, String> replaceMap = new HashMap<>();
        double chance = ObjectUtils.getValue(rewardMap.get(s), 50.0);

        replaceMap.put("{chance}", NumberUtils.get().formatDouble(chance));
        replaceMap.put("{itemStack}", s);
        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getGiveCommandRewardListPanel(), dropTable, giveRewardEditHandler);

        ServerUtils.get().runTaskAsync(() -> {
            panelBuilderCounter.getSlotsWith("Chance").forEach(slot -> panel.setOnClick(slot, getChanceAction(dropTable, giveRewardEditHandler, s)));
            panelBuilderCounter.getSlotsWith("Remove").forEach(slot -> panel.setOnClick(slot, getRemoveAction(dropTable, giveRewardEditHandler, s)));
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getChanceAction(DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler, String name) {
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
            Map<String, Double> rewards = giveRewardEditHandler.getGiveTableSubElement().getCommands();
            double currentValue = rewards.getOrDefault(name, 50.0);
            double newValue = currentValue + amountToModifyBy;

            if(newValue < 0) {
                newValue = 0;
            }

            if(newValue > 100) {
                newValue = 100;
            }

            rewards.put(name, newValue);
            giveRewardEditHandler.getGiveTableSubElement().setItems(rewards);
            saveDropTable(this.dropTableFileManager, dropTable, giveRewardEditHandler);
            openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler, name);

            Message.Boss_DropTable_RewardChance.msg(event.getWhoClicked(), modifyValue, BossAPI.getDropTableName(dropTable), NumberUtils.get().formatDouble(newValue));
        };
    }

    private ClickAction getRemoveAction(DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler, String name) {
        return event -> {
            Map<String, Double> rewards = giveRewardEditHandler.getGiveTableSubElement().getItems();

            rewards.remove(name);
            giveRewardEditHandler.getGiveTableSubElement().setItems(rewards);
            saveDropTable(this.dropTableFileManager, dropTable, giveRewardEditHandler);

            Message.Boss_DropTable_RewardRemoved.msg(event.getWhoClicked());
            this.bossPanelManager.getGiveCommandRewardListPanel().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler);
        };
    }

    private void saveDropTable(DropTableFileManager dropTableFileManager, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
        GiveTableElement giveTableElement = giveRewardEditHandler.getGiveTableElement();
        Map<String, Map<String, GiveTableSubElement>> positionMap = giveTableElement.getGiveRewards();
        Map<String, GiveTableSubElement> rewardMap = positionMap.get(giveRewardEditHandler.getDamagePosition());

        rewardMap.put(giveRewardEditHandler.getDropSection(), giveTableSubElement);
        positionMap.put(giveRewardEditHandler.getDamagePosition(), rewardMap);
        giveTableElement.setGiveRewards(positionMap);
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(giveTableElement));
        dropTableFileManager.save();
    }
}
