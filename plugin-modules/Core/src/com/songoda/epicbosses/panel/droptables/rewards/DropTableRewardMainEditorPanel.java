package com.songoda.epicbosses.panel.droptables.rewards;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.panel.droptables.rewards.interfaces.IDropTableRewardMainEditor;
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
public abstract class DropTableRewardMainEditorPanel<SubVariable> extends SubSubVariablePanelHandler<DropTable, SubVariable, String> implements IDropTableRewardMainEditor<SubVariable> {

    private DropTableFileManager dropTableFileManager;

    public DropTableRewardMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.dropTableFileManager = plugin.getDropTableFileManager();
    }

    @Override
    public void openFor(Player player, DropTable dropTable, SubVariable subVariable, String s) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();
        double chance = ObjectUtils.getValue(getChance(subVariable, s), 50.0);

        replaceMap.put("{chance}", NumberUtils.get().formatDouble(chance));
        replaceMap.put("{itemStack}", s);
        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(getParentPanelHandler(), dropTable, subVariable);

        ServerUtils.get().runTaskAsync(() -> {
            panelBuilderCounter.getSlotsWith("Chance").forEach(slot -> panel.setOnClick(slot, getChanceAction(dropTable, subVariable, s)));
            panelBuilderCounter.getSlotsWith("Remove").forEach(slot -> panel.setOnClick(slot, getRemoveAction(dropTable, subVariable, s)));
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getChanceAction(DropTable dropTable, SubVariable subVariable, String name) {
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
            Map<String, Double> rewards = getRewards(subVariable);
            double currentValue = rewards.getOrDefault(name, 50.0);
            double newValue = currentValue + amountToModifyBy;

            if(newValue < 0) {
                newValue = 0;
            }

            if(newValue > 100) {
                newValue = 100;
            }

            rewards.put(name, newValue);
            setRewards(subVariable, rewards);
            saveDropTable(this.dropTableFileManager, dropTable, subVariable);
            openFor((Player) event.getWhoClicked(), dropTable, subVariable, name);

            Message.Boss_DropTable_RewardChance.msg(event.getWhoClicked(), modifyValue, BossAPI.getDropTableName(dropTable), NumberUtils.get().formatDouble(newValue));
        };
    }

    private ClickAction getRemoveAction(DropTable dropTable, SubVariable subVariable, String name) {
        return event -> {
            Map<String, Double> rewards = getRewards(subVariable);

            rewards.remove(name);
            setRewards(subVariable, rewards);
            saveDropTable(this.dropTableFileManager, dropTable, subVariable);

            Message.Boss_DropTable_RewardRemoved.msg(event.getWhoClicked());
            getParentPanelHandler().openFor((Player) event.getWhoClicked(), dropTable, subVariable);
        };
    }
}
