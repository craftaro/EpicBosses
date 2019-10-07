package com.songoda.epicbosses.panel.droptables.types.drop;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
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
 * @since 24-Dec-18
 */
public class DropDropTableMainEditorPanel extends SubVariablePanelHandler<DropTable, DropTableElement> {

    private EpicBosses plugin;

    public DropDropTableMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, DropTableElement dropTableElement) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable, DropTableElement dropTableElement) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();
        Boolean randomDrops = dropTableElement.getRandomDrops();
        Integer maxDrops = dropTableElement.getDropMaxDrops();

        if(randomDrops == null) randomDrops = false;
        if(maxDrops == null) maxDrops = -1;

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        replaceMap.put("{randomDrops}", StringUtils.get().formatString(""+randomDrops));
        replaceMap.put("{maxDrops}", NumberUtils.get().formatDouble(maxDrops));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainDropTableEditMenu(), dropTable);

        ServerUtils.get().runTaskAsync(() -> {
            panelBuilderCounter.getSlotsWith("Rewards").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getDropDropRewardListPanel().openFor((Player) event.getWhoClicked(), dropTable, dropTableElement)));
            panelBuilderCounter.getSlotsWith("RandomDrops").forEach(slot -> panel.setOnClick(slot, getRandomDropsAction(dropTable, dropTableElement)));
            panelBuilderCounter.getSlotsWith("MaxDrops").forEach(slot -> panel.setOnClick(slot, getMaxDropsAction(dropTable, dropTableElement)));
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getRandomDropsAction(DropTable dropTable, DropTableElement dropTableElement) {
        return event -> {
            Boolean currentValue = dropTableElement.getRandomDrops();

            if(currentValue == null) currentValue = false;

            boolean newValue = !currentValue;

            dropTableElement.setRandomDrops(newValue);
            saveDropTable(dropTable, (Player) event.getWhoClicked(), dropTableElement, BossAPI.convertObjectToJsonObject(dropTableElement));

            Message.Boss_DropTable_SetRandomDrops.msg(event.getWhoClicked(), newValue);
        };
    }

    private ClickAction getMaxDropsAction(DropTable dropTable, DropTableElement dropTableElement) {
        return event -> {
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
            Integer currentAmount = dropTableElement.getDropMaxDrops();

            if(currentAmount == null) currentAmount = -1;

            int newAmount = currentAmount + amountToModifyBy;

            if(newAmount < -1) {
                newAmount = -1;
            }

            dropTableElement.setDropMaxDrops(newAmount);
            saveDropTable(dropTable, (Player) event.getWhoClicked(), dropTableElement, BossAPI.convertObjectToJsonObject(dropTableElement));

            Message.Boss_DropTable_SetMaxDrops.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newAmount));
        };
    }

    private void saveDropTable(DropTable dropTable, Player player, DropTableElement dropTableElement, JsonObject jsonObject) {
        dropTable.setRewards(jsonObject);
        this.plugin.getDropTableFileManager().save();
        openFor(player, dropTable, dropTableElement);
    }
}
