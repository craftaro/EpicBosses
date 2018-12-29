package com.songoda.epicbosses.panel.droptables.types.drop;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
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
 * @since 29-Dec-18
 */
public class DropRewardsListEditorPanel extends SubVariablePanelHandler<DropTable, DropTableElement> {

    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public DropRewardsListEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, DropTableElement dropTableElement) {
        Map<String, Double> rewardMap = dropTableElement.getDropRewards();
        List<String> keyList = new ArrayList<>(rewardMap.keySet());
        int maxPage = panel.getMaxPage(keyList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTable, dropTableElement, rewardMap, keyList);
            return true;
        }));

        loadPage(panel, 0, dropTable, dropTableElement, rewardMap, keyList);
    }

    @Override
    public void openFor(Player player, DropTable dropTable, DropTableElement dropTableElement) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getDropDropTableMainEditMenu(), dropTable, dropTableElement);

        panelBuilderCounter.getSlotsWith("NewReward").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getDropNewRewardEditMenu().openFor((Player) event.getWhoClicked(), dropTable, dropTableElement)));
        fillPanel(panel, dropTable, dropTableElement);

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, DropTable dropTable, DropTableElement dropTableElement, Map<String, Double> rewardMap, List<String> keyList) {
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

                panel.setItem(realisticSlot, itemStack, event -> this.bossPanelManager.getDropRewardMainEditMenu().openFor((Player) event.getWhoClicked(), dropTable, dropTableElement, name));
            }
        });
    }
}