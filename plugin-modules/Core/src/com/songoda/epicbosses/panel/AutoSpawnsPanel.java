package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.settings.AutoSpawnSettings;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.panel.handlers.MainListPanelHandler;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
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
 * @since 10-Oct-18
 */
public class AutoSpawnsPanel extends MainListPanelHandler {

    private AutoSpawnFileManager autoSpawnFileManager;
    private ItemsFileManager itemsFileManager;
    private EpicBosses plugin;

    public AutoSpawnsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.autoSpawnFileManager = plugin.getAutoSpawnFileManager();
        this.itemsFileManager = plugin.getItemStackManager();
        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel) {
        Map<String, AutoSpawn> autoSpawnMap = this.autoSpawnFileManager.getAutoSpawnMap();
        List<String> entryList = new ArrayList<>(autoSpawnMap.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if (requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, autoSpawnMap, entryList);
            return true;
        }));

        loadPage(panel, 0, autoSpawnMap, entryList);
    }

    private void loadPage(Panel panel, int requestedPage, Map<String, AutoSpawn> autoSpawnMap, List<String> entryList) {
        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if (slot >= entryList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {
                });
            } else {
                String name = entryList.get(slot);
                AutoSpawn autoSpawn = autoSpawnMap.get(name);
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(this.itemsFileManager.getItemStackHolder("DefaultAutoSpawnListItem"));

                if (itemStack == null) {
                    itemStack = new ItemStack(Material.BARRIER);
                }

                Map<String, String> replaceMap = new HashMap<>();
                List<String> entities = (List<String>) ObjectUtils.getValue(autoSpawn.getEntities(), new ArrayList<>());
                AutoSpawnSettings settings = autoSpawn.getAutoSpawnSettings();
                String entitiesSize = entities.size() + "";
                String maxAlive = "" + ObjectUtils.getValue(settings.getMaxAliveAtOnce(), 1);
                String amountToSpawn = "" + ObjectUtils.getValue(settings.getAmountPerSpawn(), 1);
                String chunkIsntLoaded = "" + ObjectUtils.getValue(settings.getSpawnWhenChunkIsntLoaded(), false);
                String overrideMessage = "" + ObjectUtils.getValue(settings.getOverrideDefaultSpawnMessage(), true);
                String shuffleEntities = "" + ObjectUtils.getValue(settings.getShuffleEntitiesList(), false);
                String spawnMessage = ObjectUtils.getValue(settings.getSpawnMessage(), "");

                replaceMap.put("{name}", name);
                replaceMap.put("{type}", StringUtils.get().formatString(autoSpawn.getType()));
                replaceMap.put("{enabled}", (autoSpawn.isEditing()) + "");
                replaceMap.put("{entities}", entitiesSize);
                replaceMap.put("{maxAlive}", maxAlive);
                replaceMap.put("{amountPerSpawn}", amountToSpawn);
                replaceMap.put("{chunkIsntLoaded}", chunkIsntLoaded);
                replaceMap.put("{overrideSpawnMessages}", overrideMessage);
                replaceMap.put("{shuffleEntities}", shuffleEntities);
                replaceMap.put("{customSpawnMessage}", spawnMessage);

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.AutoSpawns.Main.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getDisplay().getStringList("Display.AutoSpawns.Main.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack.clone(), e -> this.bossPanelManager.getMainAutoSpawnEditPanel().openFor((Player) e.getWhoClicked(), autoSpawn));
            }
        });
    }
}
