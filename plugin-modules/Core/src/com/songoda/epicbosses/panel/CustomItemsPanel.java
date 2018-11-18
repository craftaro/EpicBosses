package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.PanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
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
 * @since 10-Oct-18
 */
public class CustomItemsPanel extends ListPanelHandler {

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
    }

    private void loadPage(Panel panel, int requestedPage, Map<String, ItemStackHolder> currentItemStacks, List<String> entryList) {
        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if(slot >= currentItemStacks.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                ItemStackHolder itemStackHolder = currentItemStacks.get(name);
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);
                ItemStack cloneStack = itemStack.clone();

                panel.setItem(realisticSlot, itemStack, e -> {
                    if(e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {
                        int timesUsed = this.bossPanelManager.isItemStackUsed(name);

                        if(timesUsed > 0) {
                            Message.Boss_Items_CannotBeRemoved.msg(e.getWhoClicked(), timesUsed);
                        } else {
                            this.itemsFileManager.removeItemStack(name);
                            currentItemStacks.remove(name);
                            entryList.remove(name);

                            loadPage(panel, requestedPage, currentItemStacks, entryList);
                            Message.Boss_Items_Removed.msg(e.getWhoClicked());
                        }
                    } else if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
                        if(cloneStack == null) {
                            Debug.FAILED_TO_GIVE_CUSTOM_ITEM.debug(e.getWhoClicked().getName());
                            return;
                        }

                        e.getWhoClicked().getInventory().addItem(cloneStack);
                    }
                });
            }
        });
    }
}
