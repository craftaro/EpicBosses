package com.songoda.epicbosses.panel.droptables.types.spray;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
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
public class SprayDropTableMainEditorPanel extends SubVariablePanelHandler<DropTable, SprayTableElement> {

    private CustomBosses plugin;

    public SprayDropTableMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, SprayTableElement sprayTableElement) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable, SprayTableElement sprayTableElement) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();
        Boolean randomSprayDrops = sprayTableElement.getRandomSprayDrops();
        Integer maxDrops = sprayTableElement.getSprayMaxDrops();
        Integer maxDistance = sprayTableElement.getSprayMaxDistance();

        if(randomSprayDrops == null) randomSprayDrops = false;
        if(maxDrops == null) maxDrops = -1;
        if(maxDistance == null) maxDistance = 100;

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        replaceMap.put("{randomDrops}", StringUtils.get().formatString(""+randomSprayDrops));
        replaceMap.put("{maxDrops}", NumberUtils.get().formatDouble(maxDrops));
        replaceMap.put("{maxDistance}", NumberUtils.get().formatDouble(maxDistance));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainDropTableEditMenu(), dropTable);

        panelBuilderCounter.getSlotsWith("Rewards").forEach(slot -> panel.setOnClick(slot, event -> {}));
        panelBuilderCounter.getSlotsWith("RandomDrops").forEach(slot -> panel.setOnClick(slot, getRandomDropsAction(dropTable, sprayTableElement)));
        panelBuilderCounter.getSlotsWith("MaxDistance").forEach(slot -> panel.setOnClick(slot, getMaxDistanceAction(dropTable, sprayTableElement)));
        panelBuilderCounter.getSlotsWith("MaxDrops").forEach(slot -> panel.setOnClick(slot, getMaxDropsAction(dropTable, sprayTableElement)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getRandomDropsAction(DropTable dropTable, SprayTableElement sprayTableElement) {
        return event -> {
            Boolean currentValue = sprayTableElement.getRandomSprayDrops();

            if(currentValue == null) currentValue = false;

            boolean newValue = !currentValue;

            sprayTableElement.setRandomSprayDrops(newValue);
            saveDropTable(dropTable, (Player) event.getWhoClicked(), sprayTableElement, BossAPI.convertObjectToJsonObject(sprayTableElement));

            Message.Boss_DropTable_SetRandomDrops.msg(event.getWhoClicked(), newValue);
        };
    }

    private ClickAction getMaxDistanceAction(DropTable dropTable, SprayTableElement sprayTableElement) {
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
            Integer currentAmount = sprayTableElement.getSprayMaxDistance();

            if(currentAmount == null) currentAmount = 100;

            int newAmount = currentAmount + amountToModifyBy;

            if(newAmount < 0) {
                newAmount = 0;
            }

            sprayTableElement.setSprayMaxDistance(newAmount);
            saveDropTable(dropTable, (Player) event.getWhoClicked(), sprayTableElement, BossAPI.convertObjectToJsonObject(sprayTableElement));

            Message.Boss_DropTable_SetMaxDistance.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newAmount));
        };
    }

    private ClickAction getMaxDropsAction(DropTable dropTable, SprayTableElement sprayTableElement) {
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
            Integer currentAmount = sprayTableElement.getSprayMaxDrops();

            if(currentAmount == null) currentAmount = -1;

            int newAmount = currentAmount + amountToModifyBy;

            if(newAmount < -1) {
                newAmount = -1;
            }

            sprayTableElement.setSprayMaxDrops(newAmount);
            saveDropTable(dropTable, (Player) event.getWhoClicked(), sprayTableElement, BossAPI.convertObjectToJsonObject(sprayTableElement));

            Message.Boss_DropTable_SetMaxDrops.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newAmount));
        };
    }

    private void saveDropTable(DropTable dropTable, Player player, SprayTableElement sprayTableElement, JsonObject jsonObject) {
        dropTable.setRewards(jsonObject);
        this.plugin.getDropTableFileManager().save();
        openFor(player, dropTable, sprayTableElement);
    }
}
