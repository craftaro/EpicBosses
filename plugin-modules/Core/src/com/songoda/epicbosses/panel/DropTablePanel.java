package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.panel.handlers.MainListPanelHandler;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
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
public class DropTablePanel extends MainListPanelHandler {

    private DropTableFileManager dropTableFileManager;
    private ItemStackConverter itemStackConverter;
    private CustomBosses plugin;

    public DropTablePanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.dropTableFileManager = plugin.getDropTableFileManager();
    }

    @Override
    public void fillPanel(Panel panel) {
        Map<String, DropTable> dropTableMap = this.dropTableFileManager.getDropTables();
        List<String> entryList = new ArrayList<>(dropTableMap.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTableMap, entryList);
            return true;
        }));

        loadPage(panel, 0, dropTableMap, entryList);
    }

    private void loadPage(Panel panel, int requestedPage, Map<String, DropTable> dropTableMap, List<String> entryList) {
        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if(slot >= dropTableMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                DropTable dropTable = dropTableMap.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultDropTableMenuItem");
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                if(itemStack == null) {
                    itemStack = new ItemStack(Material.BARRIER);
                }

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);
                replaceMap.put("{type}", StringUtils.get().formatString(dropTable.getDropType()));

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.DropTable.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.DropTable.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, e -> {
                    //TODO: Add Drop Table editing
                });
            }
        });
    }
}
