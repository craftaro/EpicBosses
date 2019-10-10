package com.songoda.epicbosses.panel.droptables.types.give.commands;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class GiveCommandRewardListPanel extends SubVariablePanelHandler<DropTable, GiveRewardEditHandler> {

    private ItemsFileManager itemsFileManager;
    private EpicBosses plugin;

    public GiveCommandRewardListPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        Map<String, Double> rewardMap = getRewards(giveRewardEditHandler);
        List<String> keyList = new ArrayList<>(rewardMap.keySet());
        int maxPage = panel.getMaxPage(keyList);

        panel.setOnPageChange((player, currentPage, requestedPage) -> {
            if (requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTable, giveRewardEditHandler, rewardMap, keyList);
            return true;
        });

        loadPage(panel, 0, dropTable, giveRewardEditHandler, rewardMap, keyList);
    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getGiveRewardMainEditMenu(), dropTable, giveRewardEditHandler);

        ServerUtils.get().runTaskAsync(() -> {
            panelBuilderCounter.getSlotsWith("NewReward").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getGiveCommandNewRewardPanel().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler)));
            fillPanel(panel, dropTable, giveRewardEditHandler);
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler, Map<String, Double> rewardMap, List<String> keyList) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if (slot >= keyList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {
                });
            } else {
                String name = keyList.get(slot);
                Double chance = rewardMap.get(name);
                Map<String, String> replaceMap = new HashMap<>();

                if (chance == null) chance = 100.0;

                replaceMap.put("{commandName}", name);
                replaceMap.put("{chance}", NumberUtils.get().formatDouble(chance));

                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultTextMenuItem");
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.DropTable.CommandRewardList.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getDisplay().getStringList("Display.DropTable.CommandRewardList.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, event -> this.bossPanelManager.getGiveCommandRewardMainEditMenu().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler, name));
            }
        });
    }

    private Map<String, Double> getRewards(GiveRewardEditHandler giveRewardEditHandler) {
        return giveRewardEditHandler.getGiveTableSubElement().getCommands();
    }
}
