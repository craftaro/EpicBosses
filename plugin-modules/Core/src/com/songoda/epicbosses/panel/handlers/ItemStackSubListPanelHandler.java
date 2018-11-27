package com.songoda.epicbosses.panel.handlers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Nov-18
 */
public abstract class ItemStackSubListPanelHandler extends SubVariablePanelHandler<BossEntity, EntityStatsElement> {

    protected ItemStackConverter itemStackConverter;

    private BossesFileManager bossesFileManager;
    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public ItemStackSubListPanelHandler(BossPanelManager bossPanelManager, ConfigurationSection configurationSection, CustomBosses plugin) {
        super(bossPanelManager, configurationSection);

        this.plugin = plugin;
        this.itemsFileManager = plugin.getItemStackManager();
        this.bossesFileManager = plugin.getBossesFileManager();
        this.itemStackConverter = new ItemStackConverter();
    }

    public abstract Map<String, ItemStackHolder> getFilteredMap(Map<String, ItemStackHolder> originalMap);

    public abstract ISubVariablePanelHandler<BossEntity, EntityStatsElement> getParentHolder();

    public abstract void getUpdateAction(EntityStatsElement entityStatsElement, String newName);

    public abstract String getCurrent(EntityStatsElement entityStatsElement);

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        Map<String, ItemStackHolder> itemStackHolderMap = this.itemsFileManager.getItemStackHolders();
        Map<String, ItemStackHolder> filteredMap = getFilteredMap(itemStackHolderMap);
        List<String> entryList = new ArrayList<>(filteredMap.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, filteredMap, entryList, bossEntity, entityStatsElement);
            return true;
        }));

        loadPage(panel, 0, filteredMap, entryList, bossEntity, entityStatsElement);
    }

    @Override
    public void openFor(Player player, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));

        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelLowerClick(true)
                .setCancelClick(true)
                .setParentPanelHandler(getParentHolder(), bossEntity, entityStatsElement);
        PanelBuilderCounter panelBuilderCounter = panel.getPanelBuilderCounter();

        panelBuilderCounter.getSlotsWith("AddNew").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getAddItemsMenu().openFor(player)));
        panelBuilderCounter.getSlotsWith("Remove").forEach(slot -> panel.setOnClick(slot, event -> {
            getUpdateAction(entityStatsElement, "");
            this.bossesFileManager.save();

            openFor((Player) event.getWhoClicked(), bossEntity, entityStatsElement);
        }));

        fillPanel(panel, bossEntity, entityStatsElement);

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("Remove")
                .addSlotCounter("AddNew");


    }

    private void loadPage(Panel panel, int requestedPage, Map<String, ItemStackHolder> filteredMap, List<String> entryList, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        String current = getCurrent(entityStatsElement);

        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if(slot >= filteredMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                ItemStackHolder itemStackHolder = filteredMap.get(name);
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                if(itemStack == null) {
                    itemStack = new ItemStack(Material.BARRIER);
                }

                if(name.equalsIgnoreCase(current)) {
                    Map<String, String> replaceMap = new HashMap<>();

                    replaceMap.put("{name}", ItemStackUtils.getName(itemStack));

                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Equipment.name"), replaceMap);
                }

                panel.setItem(realisticSlot, itemStack, e -> {
                    getUpdateAction(entityStatsElement, name);
                    this.bossesFileManager.save();

                    openFor((Player) e.getWhoClicked(), bossEntity, entityStatsElement);
                });
            }
        });
    }
}
