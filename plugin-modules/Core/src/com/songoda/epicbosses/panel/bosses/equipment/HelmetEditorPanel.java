package com.songoda.epicbosses.panel.bosses.equipment;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
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
 * @since 19-Nov-18
 */
public class HelmetEditorPanel extends SubVariablePanelHandler<BossEntity, EntityStatsElement> {

    private ItemStackConverter itemStackConverter;
    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public HelmetEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        Map<String, ItemStackHolder> itemStackHolderMap = this.itemsFileManager.getItemStackHolders();
        List<String> entryList = new ArrayList<>(itemStackHolderMap.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, itemStackHolderMap, entryList, entityStatsElement);
            return true;
        }));

        loadPage(panel, 0, itemStackHolderMap, entryList, entityStatsElement);
    }

    @Override
    public void openFor(Player player, BossEntity bossEntity, EntityStatsElement entityStatsElement) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int requestedPage, Map<String, ItemStackHolder> itemStackHolderMap, List<String> entryList, EntityStatsElement entityStatsElement) {
        String helmet = entityStatsElement.getEquipment().getHelmet();

        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if(slot >= itemStackHolderMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                ItemStackHolder itemStackHolder = itemStackHolderMap.get(name);
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                if(itemStack == null) {
                    itemStack = new ItemStack(Material.BARRIER);
                }

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", ItemStackUtils.getName(itemStack));

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.EquipmentEditor.name"), replaceMap);

                panel.setItem(realisticSlot, itemStack, e -> {

                });
            }
        });
    }
}
