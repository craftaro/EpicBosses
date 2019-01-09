package com.songoda.epicbosses.panel.droptables.types.give;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 31-Dec-18
 */
public class GiveRewardRewardsListPanel extends SubSubVariablePanelHandler<DropTable, GiveTableElement, String> {

    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public GiveRewardRewardsListPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveTableElement giveTableElement, String s) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getGiveRewardPositionListMenu(), dropTable, giveTableElement);

        ServerUtils.get().runTaskAsync(() -> {
            panelBuilderCounter.getSlotsWith("NewRewardSection").forEach(slot -> panel.setOnClick(slot, getNewRewardSectionAction(dropTable, giveTableElement, s)));
            fillPanel(panel, dropTable, giveTableElement, s);
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void fillPanel(Panel panel, DropTable dropTable, GiveTableElement giveTableElement, String key) {
        Map<String, Map<String, GiveTableSubElement>> rewardSections = giveTableElement.getGiveRewards();
        Map<String, GiveTableSubElement> rewards = rewardSections.get(key);
        List<String> keys = new ArrayList<>(rewards.keySet());
        int maxPage = panel.getMaxPage(keys);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTable, giveTableElement, key, keys, rewardSections);
            return true;
        }));

        loadPage(panel, 0, dropTable, giveTableElement, key, keys, rewardSections);
    }

    private void loadPage(Panel panel, int page, DropTable dropTable, GiveTableElement giveTableElement, String key, List<String> keys, Map<String, Map<String, GiveTableSubElement>> rewardSections) {
        Map<String, GiveTableSubElement> rewards = rewardSections.get(key);
        NumberUtils numberUtils = NumberUtils.get();

        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= keys.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                String rewardSectionPosition = keys.get(slot);
                GiveTableSubElement giveTableSubElement = rewards.get(rewardSectionPosition);
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(this.itemsFileManager.getItemStackHolder("DefaultDropTableRewardsListItem"));

                int position = NumberUtils.get().getInteger(key);
                Integer itemDrops = giveTableSubElement.getItems().size();
                Integer commandDrops = giveTableSubElement.getCommands().size();
                Double requiredPercentage = ObjectUtils.getValue(giveTableSubElement.getRequiredPercentage(), 0.0);
                Integer maxDrops = ObjectUtils.getValue(giveTableSubElement.getMaxDrops(), 3);
                Integer maxCommands = ObjectUtils.getValue(giveTableSubElement.getMaxCommands(), 3);
                Boolean randomDrops = ObjectUtils.getValue(giveTableSubElement.getRandomDrops(), false);
                Boolean randomCommands = ObjectUtils.getValue(giveTableSubElement.getRandomCommands(), false);
                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{position}", numberUtils.formatDouble(position));
                replaceMap.put("{percentage}", numberUtils.formatDouble(requiredPercentage));
                replaceMap.put("{items}", numberUtils.formatDouble(itemDrops));
                replaceMap.put("{maxDrops}", numberUtils.formatDouble(maxDrops));
                replaceMap.put("{randomDrops}", ""+randomDrops);
                replaceMap.put("{commands}", numberUtils.formatDouble(commandDrops));
                replaceMap.put("{maxCommands}", numberUtils.formatDouble(maxCommands));
                replaceMap.put("{randomCommands}", ""+randomCommands);

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.DropTable.GiveRewardsList.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.DropTable.GiveRewardsList.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, event -> {
                    ClickType clickType = event.getClick();

                    if(clickType == ClickType.SHIFT_RIGHT) {
                        rewards.remove(rewardSectionPosition);
                        rewardSections.put(key, rewards);
                        giveTableElement.setGiveRewards(rewardSections);
                        saveDropTable((Player) event.getWhoClicked(), dropTable, giveTableElement, key);
                    } else {
                        GiveRewardEditHandler giveRewardEditHandler = new GiveRewardEditHandler(key, rewardSectionPosition, dropTable, giveTableElement, giveTableSubElement);

                        this.bossPanelManager.getGiveRewardMainEditMenu().openFor((Player) event.getWhoClicked(), dropTable, giveRewardEditHandler);
                    }
                });
            }
        });
    }

    private ClickAction getNewRewardSectionAction(DropTable dropTable, GiveTableElement giveTableElement, String key) {
        return event -> {
            Map<String, Map<String, GiveTableSubElement>> rewardSections = giveTableElement.getGiveRewards();
            Map<String, GiveTableSubElement> rewards = rewardSections.get(key);
            List<String> keys = new ArrayList<>(rewards.keySet());
            int nextAvailable = NumberUtils.get().getNextAvailablePosition(keys);
            String nextKey = ""+nextAvailable;

            if(rewards.containsKey(nextKey)) {
                Debug.FAILED_TO_CREATE_NEWPOSITION.debug(nextKey, BossAPI.getDropTableName(dropTable));
                return;
            }

            rewards.put(nextKey, new GiveTableSubElement(new HashMap<>(), new HashMap<>(), 3, 3, false, false, 0.0));
            rewardSections.put(key, rewards);
            giveTableElement.setGiveRewards(rewardSections);
            saveDropTable((Player) event.getWhoClicked(), dropTable, giveTableElement, key);
        };
    }

    private void saveDropTable(Player player, DropTable dropTable, GiveTableElement giveTableElement, String string) {
        dropTable.setRewards(BossAPI.convertObjectToJsonObject(giveTableElement));
        this.plugin.getDropTableFileManager().save();
        openFor(player, dropTable, giveTableElement, string);
    }
}
