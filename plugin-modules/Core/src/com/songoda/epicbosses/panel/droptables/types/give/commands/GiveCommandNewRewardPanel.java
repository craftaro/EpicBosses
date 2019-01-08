package com.songoda.epicbosses.panel.droptables.types.give.commands;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class GiveCommandNewRewardPanel extends SubVariablePanelHandler<DropTable, GiveRewardEditHandler> {

    private DropTableFileManager dropTableFileManager;
    private CommandsFileManager commandsFileManager;
    private ItemStackConverter itemStackConverter;
    private CustomBosses plugin;

    public GiveCommandNewRewardPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.commandsFileManager = plugin.getBossCommandFileManager();
        this.dropTableFileManager = plugin.getDropTableFileManager();
        this.itemStackConverter = new ItemStackConverter();
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
        ServerUtils.get().runTaskAsync(() -> {
            Panel panel = getPanelBuilder().getPanel()
                    .setParentPanelHandler(this.bossPanelManager.getGiveCommandRewardListPanel(), dropTable, giveRewardEditHandler);

            fillPanel(panel, dropTable, giveRewardEditHandler);
            panel.openFor(player);
        });
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler, List<String> filteredKeys, Map<String, List<String>> commands) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= filteredKeys.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                String name = filteredKeys.get(slot);
                List<String> innerCommands = commands.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultTextMenuItem");
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Commands.name"), replaceMap);

                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> presetLore = this.plugin.getConfig().getStringList("Display.Boss.Commands.lore");
                List<String> newLore = new ArrayList<>();

                for(String s : presetLore) {
                    if(s.contains("{commands}")) {
                        for(String command : innerCommands) {
                            newLore.add(StringUtils.get().translateColor("&7" + command));
                        }
                    } else {
                        newLore.add(StringUtils.get().translateColor(s));
                    }
                }

                itemMeta.setLore(newLore);
                itemStack.setItemMeta(itemMeta);

                panel.setItem(realisticSlot, itemStack, event -> {
                    Map<String, Double> rewards = giveRewardEditHandler.getGiveTableSubElement().getCommands();

                    rewards.put(name, 50.0);
                    saveDropTable(this.dropTableFileManager, dropTable, giveRewardEditHandler);

                    this.bossPanelManager.getGiveCommandRewardMainEditMenu().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler, name);
                    Message.Boss_DropTable_AddedNewReward.msg(event.getWhoClicked(), BossAPI.getDropTableName(dropTable));
                });
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
