package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.panel.handlers.MainListPanelHandler;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class CustomItemsPanel extends MainListPanelHandler {

    private ItemsFileManager itemsFileManager;

    public CustomItemsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public void fillPanel(Panel panel) {
        Map<String, ItemStackHolder> currentItemStacks = this.itemsFileManager.getItemStackHolders();
        List<String> entryList = new ArrayList<>(currentItemStacks.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentItemStacks, entryList);
            return true;
        }));

        loadPage(panel, 0, currentItemStacks, entryList);
        panel.getPanelBuilderCounter().getSlotsWith("AddNew").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getAddItemsMenu().openFor((Player) event.getWhoClicked())));
    }

    private void loadPage(Panel panel, int requestedPage, Map<String, ItemStackHolder> currentItemStacks, List<String> entryList) {
        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if(slot >= currentItemStacks.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                ItemStackHolder itemStackHolder = currentItemStacks.get(name);
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);

                panel.setItem(realisticSlot, itemStack.clone(), e -> {
                    ClickType clickType = e.getClick();

                    if(clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
                        int timesUsed = this.bossPanelManager.isItemStackUsed(name);

                        if(timesUsed > 0) {
                            Message.Boss_Items_CannotBeRemoved.msg(e.getWhoClicked(), timesUsed);
                        } else if(name.contains("Default")) {
                            Message.Boss_Items_DefaultCannotBeRemoved.msg(e.getWhoClicked(), timesUsed);
                        } else {
                            this.itemsFileManager.removeItemStack(name);
                            currentItemStacks.remove(name);
                            entryList.remove(name);

                            loadPage(panel, requestedPage, currentItemStacks, entryList);
                            Message.Boss_Items_Removed.msg(e.getWhoClicked());
                        }
                    } else if(clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
                        e.getWhoClicked().getInventory().addItem(itemStack.clone());
                    } else if(clickType == ClickType.MIDDLE) {
                        String newName = UUID.randomUUID().toString();
                        ItemStack newItemStack = itemStack.clone();

                        Message.Boss_Items_Cloned.msg(e.getWhoClicked(), newName);
                        this.itemsFileManager.addItemStack(newName, newItemStack);
                        fillPanel(panel);
                    }
                });
            }
        });
    }
}
