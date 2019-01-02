package com.songoda.epicbosses.panel.droptables.types.give.commands;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class GiveCommandNewRewardPanel extends SubVariablePanelHandler<DropTable, GiveRewardEditHandler> {

    private CommandsFileManager commandsFileManager;
    private CustomBosses plugin;

    public GiveCommandNewRewardPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.commandsFileManager = plugin.getBossCommandFileManager();
        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        Map<String, List<String>> commands = this.commandsFileManager.getCommandsMap();
        List<String> currentKeys = getCurrentKeys(giveRewardEditHandler);
        List<String> filteredKeys = getFilteredKeys(commands, currentKeys);
        int maxPage = panel.getMaxPage(filteredKeys);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTable, giveRewardEditHandler, filteredKeys, commands);
            return true;
        }));

        loadPage(panel, 0, dropTable, giveRewardEditHandler, filteredKeys, commands);
    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        Panel panel = getPanelBuilder().getPanel();
//                .setParentPanelHandler(this.bossPanelManager.getGiveCommandRewardListPanel(), dropTable, giveRewardEditHandler);

        fillPanel(panel, dropTable, giveRewardEditHandler);
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, DropTable dropTable, GiveRewardEditHandler subVariable, List<String> filteredKeys, Map<String, List<String>> commands) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= filteredKeys.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                String name = filteredKeys.get(slot);
//                ItemStackHolder itemStackHolder = itemStacks.get(name);
//                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);

//                panel.setItem(realisticSlot, itemStack, event -> {
//                    Map<String, Double> currentRewards = getRewards(subVariable);
//
//                    currentRewards.put(name, 50.0);
//                    saveDropTable(this.plugin.getDropTableFileManager(), dropTable, subVariable);
//
//                    getRewardMainEditMenu().openFor((Player) event.getWhoClicked(), dropTable, subVariable, name);
//                    Message.Boss_DropTable_AddedNewReward.msg(event.getWhoClicked(), BossAPI.getDropTableName(dropTable));
//                });
            }
        });
    }


    private List<String> getCurrentKeys(GiveRewardEditHandler giveRewardEditHandler) {
        return new ArrayList<>(giveRewardEditHandler.getGiveTableSubElement().getCommands().keySet());
    }

    private List<String> getFilteredKeys(Map<String, List<String>> commands, List<String> currentKeys) {
        List<String> filteredList = new ArrayList<>();

        commands.keySet().forEach(string -> {
            if(currentKeys.contains(string)) return;

            filteredList.add(string);
        });

        return filteredList;
    }
}
