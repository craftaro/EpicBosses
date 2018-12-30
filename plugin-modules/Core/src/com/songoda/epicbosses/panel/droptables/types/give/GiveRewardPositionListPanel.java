package com.songoda.epicbosses.panel.droptables.types.give;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Dec-18
 */
public class GiveRewardPositionListPanel extends SubVariablePanelHandler<DropTable, GiveTableElement> {

    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public GiveRewardPositionListPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, GiveTableElement giveTableElement) {
        Map<String, Map<String, GiveTableSubElement>> rewardSections = giveTableElement.getGiveRewards();
        List<String> keys = new ArrayList<>(rewardSections.keySet());
        int maxPage = panel.getMaxPage(keys);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTable, giveTableElement, keys, rewardSections);
            return true;
        }));

        loadPage(panel, 0, dropTable, giveTableElement, keys, rewardSections);
    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveTableElement giveTableElement) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainDropTableEditMenu(), dropTable);

        panelBuilderCounter.getSlotsWith("NewPosition").forEach(slot -> panel.setOnClick(slot, getNewPositionAction(dropTable, giveTableElement)));
        fillPanel(panel, dropTable, giveTableElement);
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, DropTable dropTable, GiveTableElement giveTableElement, List<String> keys, Map<String, Map<String, GiveTableSubElement>> rewards) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= keys.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                String position = keys.get(slot);
                Map<String, GiveTableSubElement> innerRewards = rewards.get(position);
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(this.itemsFileManager.getItemStackHolder("DefaultDropTableRewardItem"));
                int dropAmount = innerRewards.keySet().size();
                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{position}", NumberUtils.get().formatDouble(Integer.valueOf(position)));
                replaceMap.put("{dropAmount}", NumberUtils.get().formatDouble(dropAmount));

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.DropTable.GivePositionList.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.DropTable.GivePositionList.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, event -> {
                    ClickType clickType = event.getClick();

                    if(clickType == ClickType.SHIFT_RIGHT) {
                        rewards.remove(position);
                        giveTableElement.setGiveRewards(rewards);
                        saveDropTable((Player) event.getWhoClicked(), dropTable, giveTableElement, BossAPI.convertObjectToJsonObject(giveTableElement));
                    } else {
                        this.bossPanelManager.getGiveRewardRewardsListMenu().openFor((Player) event.getWhoClicked(), dropTable, giveTableElement, position);
                    }
                });
            }
        });
    }

    private ClickAction getNewPositionAction(DropTable dropTable, GiveTableElement giveTableElement) {
        return event -> {
            Map<String, Map<String, GiveTableSubElement>> rewards = giveTableElement.getGiveRewards();
            List<String> keys = new ArrayList<>(giveTableElement.getGiveRewards().keySet());
            int nextAvailable = NumberUtils.get().getNextAvailablePosition(keys);
            String nextKey = ""+nextAvailable;

            if(rewards.containsKey(nextKey)) {
                Debug.FAILED_TO_CREATE_NEWPOSITION.debug(nextKey, BossAPI.getDropTableName(dropTable));
                return;
            }

            rewards.put(nextKey, new HashMap<>());
            giveTableElement.setGiveRewards(rewards);
            saveDropTable((Player) event.getWhoClicked(), dropTable, giveTableElement, BossAPI.convertObjectToJsonObject(giveTableElement));
        };
    }

    private void saveDropTable(Player player, DropTable dropTable, GiveTableElement giveTableElement, JsonObject jsonObject) {
        dropTable.setRewards(jsonObject);
        this.plugin.getDropTableFileManager().save();
        openFor(player, dropTable, giveTableElement);
    }
}
