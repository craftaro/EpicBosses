package com.songoda.epicbosses.panel.droptables.types.give;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 31-Dec-18
 */
public class GiveRewardMainEditPanel extends SubVariablePanelHandler<DropTable, GiveRewardEditHandler> {

    private CustomBosses plugin;

    public GiveRewardMainEditPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        ServerUtils.get().runTaskAsync(() -> {
            PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
            Map<String, String> replaceMap = new HashMap<>();
            GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
            Integer itemDrops = giveTableSubElement.getItems().size();
            Integer commandDrops = giveTableSubElement.getCommands().size();
            Double requiredPercentage = ObjectUtils.getValue(giveTableSubElement.getRequiredPercentage(), 0.0);
            Integer maxDrops = ObjectUtils.getValue(giveTableSubElement.getMaxDrops(), 3);
            Integer maxCommands = ObjectUtils.getValue(giveTableSubElement.getMaxCommands(), 3);
            Boolean randomDrops = ObjectUtils.getValue(giveTableSubElement.getRandomDrops(), false);
            Boolean randomCommands = ObjectUtils.getValue(giveTableSubElement.getRandomCommands(), false);

            replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
            replaceMap.put("{position}", giveRewardEditHandler.getDamagePosition());
            replaceMap.put("{randomDrops}", ""+randomDrops);
            replaceMap.put("{maxDrops}", NumberUtils.get().formatDouble(maxDrops));
            replaceMap.put("{drops}", NumberUtils.get().formatDouble(itemDrops));
            replaceMap.put("{requiredPercentage}", NumberUtils.get().formatDouble(requiredPercentage));
            replaceMap.put("{commands}", NumberUtils.get().formatDouble(commandDrops));
            replaceMap.put("{maxCommands}", NumberUtils.get().formatDouble(maxCommands));
            replaceMap.put("{randomCommands}", ""+randomCommands);
            panelBuilder.addReplaceData(replaceMap);

            PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();
            Panel panel = panelBuilder.getPanel()
                    .setParentPanelHandler(this.bossPanelManager.getGiveRewardRewardsListMenu(), dropTable, giveRewardEditHandler.getGiveTableElement(), giveRewardEditHandler.getDamagePosition());

            counter.getSlotsWith("RandomDrops").forEach(slot -> panel.setOnClick(slot, getRandomDropsAction(dropTable, giveRewardEditHandler)));
            counter.getSlotsWith("MaxDrops").forEach(slot -> panel.setOnClick(slot, getMaxDropsAction(dropTable, giveRewardEditHandler)));
            counter.getSlotsWith("ItemDrops").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getGiveDropRewardListPanel().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler)));
            counter.getSlotsWith("RequiredPercentage").forEach(slot -> panel.setOnClick(slot, getRequiredPercentageAction(dropTable, giveRewardEditHandler)));
            counter.getSlotsWith("CommandDrops").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getGiveCommandRewardListPanel().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler)));
            counter.getSlotsWith("MaxCommands").forEach(slot -> panel.setOnClick(slot, getMaxCommandsAction(dropTable, giveRewardEditHandler)));
            counter.getSlotsWith("RandomDrops").forEach(slot -> panel.setOnClick(slot, getRandomCommandsAction(dropTable, giveRewardEditHandler)));

            panel.openFor(player);
        });
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getRequiredPercentageAction(DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        return event -> {
            GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
            ClickType clickType = event.getClick();
            double amountToModifyBy;

            if(clickType == ClickType.SHIFT_LEFT) {
                amountToModifyBy = 10;
            } else if(clickType == ClickType.RIGHT) {
                amountToModifyBy = -1;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                amountToModifyBy = -10;
            } else {
                amountToModifyBy = 1;
            }

            String modifyValue = amountToModifyBy > 0? "increased" : "decreased";
            double currentAmount = ObjectUtils.getValue(giveTableSubElement.getRequiredPercentage(), 0.0);

            double newAmount = currentAmount + amountToModifyBy;

            if(newAmount < 0) {
                newAmount = 0;
            }

            if(newAmount > 100) {
                newAmount = 100;
            }

            giveTableSubElement.setRequiredPercentage(newAmount);
            saveDropTable(dropTable, giveRewardEditHandler, event);

            Message.Boss_DropTable_GiveMaxCommands.msg(event.getWhoClicked(), modifyValue, BossAPI.getDropTableName(dropTable), NumberUtils.get().formatDouble(newAmount));
        };
    }

    private ClickAction getMaxCommandsAction(DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        return event -> {
            GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
            ClickType clickType = event.getClick();
            int amountToModifyBy;

            if(clickType == ClickType.SHIFT_LEFT) {
                amountToModifyBy = 10;
            } else if(clickType == ClickType.RIGHT) {
                amountToModifyBy = -1;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                amountToModifyBy = -10;
            } else {
                amountToModifyBy = 1;
            }

            String modifyValue = amountToModifyBy > 0? "increased" : "decreased";
            int currentAmount = ObjectUtils.getValue(giveTableSubElement.getMaxCommands(), 3);

            int newAmount = currentAmount + amountToModifyBy;

            if(newAmount < -1) {
                newAmount = -1;
            }

            giveTableSubElement.setMaxCommands(newAmount);
            saveDropTable(dropTable, giveRewardEditHandler, event);

            Message.Boss_DropTable_GiveMaxCommands.msg(event.getWhoClicked(), modifyValue, BossAPI.getDropTableName(dropTable), NumberUtils.get().formatDouble(newAmount));
        };
    }

    private ClickAction getMaxDropsAction(DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        return event -> {
            GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
            ClickType clickType = event.getClick();
            int amountToModifyBy;

            if(clickType == ClickType.SHIFT_LEFT) {
                amountToModifyBy = 10;
            } else if(clickType == ClickType.RIGHT) {
                amountToModifyBy = -1;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                amountToModifyBy = -10;
            } else {
                amountToModifyBy = 1;
            }

            String modifyValue = amountToModifyBy > 0? "increased" : "decreased";
            int currentAmount = ObjectUtils.getValue(giveTableSubElement.getMaxDrops(), 3);

            int newAmount = currentAmount + amountToModifyBy;

            if(newAmount < -1) {
                newAmount = -1;
            }

            giveTableSubElement.setMaxDrops(newAmount);
            saveDropTable(dropTable, giveRewardEditHandler, event);

            Message.Boss_DropTable_GiveMaxDrops.msg(event.getWhoClicked(), modifyValue, BossAPI.getDropTableName(dropTable), NumberUtils.get().formatDouble(newAmount));
        };
    }

    private ClickAction getRandomDropsAction(DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        return event -> {
            GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
            boolean randomValue = ObjectUtils.getValue(giveTableSubElement.getRandomDrops(), false);
            boolean newValue = !randomValue;

            giveTableSubElement.setRandomDrops(newValue);
            saveDropTable(dropTable, giveRewardEditHandler, event);

            Message.Boss_DropTable_GiveRandomDrops.msg(event.getWhoClicked(), BossAPI.getDropTableName(dropTable), newValue);
        };
    }

    private ClickAction getRandomCommandsAction(DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        return event -> {
            GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
            boolean randomValue = ObjectUtils.getValue(giveTableSubElement.getRandomCommands(), false);
            boolean newValue = !randomValue;

            giveTableSubElement.setRandomCommands(newValue);
            saveDropTable(dropTable, giveRewardEditHandler, event);

            Message.Boss_DropTable_GiveRandomDrops.msg(event.getWhoClicked(), BossAPI.getDropTableName(dropTable), newValue);
        };
    }

    private void saveDropTable(DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler, InventoryClickEvent event) {
        GiveTableElement giveTableElement = giveRewardEditHandler.getGiveTableElement();
        GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
        Map<String, Map<String, GiveTableSubElement>> rewardMap = giveTableElement.getGiveRewards();
        String itemSlot = giveRewardEditHandler.getDropSection();
        String damagePosition = giveRewardEditHandler.getDamagePosition();
        Map<String, GiveTableSubElement> itemMap = rewardMap.get(damagePosition);

        itemMap.put(itemSlot, giveTableSubElement);
        rewardMap.put(damagePosition, itemMap);
        giveTableElement.setGiveRewards(rewardMap);
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(giveTableElement));
        this.plugin.getDropTableFileManager().save();

        openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler);
    }
}
