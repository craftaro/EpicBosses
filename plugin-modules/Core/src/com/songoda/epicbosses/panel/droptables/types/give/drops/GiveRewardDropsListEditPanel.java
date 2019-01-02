package com.songoda.epicbosses.panel.droptables.types.give.drops;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
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
public class GiveRewardDropsListEditPanel extends SubVariablePanelHandler<DropTable, GiveRewardEditHandler> {

    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public GiveRewardDropsListEditPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.itemsFileManager = plugin.getItemStackManager();
        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
        Map<String, Double> rewardMap = giveTableSubElement.getItems();
        List<String> keyList = new ArrayList<>();
        int maxPage = panel.getMaxPage(keyList);

        panel.setOnPageChange((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

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

//        panelBuilderCounter.getSlotsWith("NewReward").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getSprayNewRewardEditMenu().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler)));
        fillPanel(panel, dropTable, giveRewardEditHandler);

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler, Map<String, Double> rewardMap, List<String> keyList) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= keyList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = keyList.get(slot);
                Double chance = rewardMap.get(name);
                Map<String, String> replaceMap = new HashMap<>();

                if(chance == null) chance = 100.0;

                replaceMap.put("{itemName}", name);
                replaceMap.put("{chance}", NumberUtils.get().formatDouble(chance));

                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(this.itemsFileManager.getItemStackHolder(name));

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.DropTable.RewardList.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.DropTable.RewardList.lore"), replaceMap);

//                panel.setItem(realisticSlot, itemStack, event -> this.bossPanelManager.getSprayRewardMainEditMenu().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler, name));
            }
        });
    }
}
