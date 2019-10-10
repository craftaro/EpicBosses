package com.songoda.epicbosses.panel;

import com.songoda.core.hooks.EconomyManager;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.PanelHandler;
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
 * @since 17-Nov-18
 */
public class ShopPanel extends PanelHandler {

    private BossEntityManager bossEntityManager;
    private BossesFileManager bossesFileManager;
    private EpicBosses plugin;

    public ShopPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.bossEntityManager = plugin.getBossEntityManager();
        this.bossesFileManager = plugin.getBossesFileManager();
    }

    @Override
    public void fillPanel(Panel panel) {
        Map<String, BossEntity> currentEntities = this.bossesFileManager.getBossEntitiesMap();
        Map<String, BossEntity> filteredMap = getFilteredMap(currentEntities);
        List<String> entryList = new ArrayList<>(filteredMap.keySet());
        int maxPage = panel.getMaxPage(filteredMap);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if (requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, entryList, filteredMap);
            return true;
        }));

        loadPage(panel, 0, entryList, filteredMap);
    }

    @Override
    public void openFor(Player player) {
        Panel panel = getPanelBuilder().getPanel();

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel));
        panel.openFor(player);
    }

    private void loadPage(Panel panel, int page, List<String> entryList, Map<String, BossEntity> filteredMap) {
        panel.loadPage(page, ((slot, realisticSlot) -> {
            if (slot >= filteredMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {
                });
            } else {
                String name = entryList.get(slot);
                BossEntity bossEntity = filteredMap.get(name);
                ItemStack itemStack = this.bossEntityManager.getDisplaySpawnItem(bossEntity);
                double price = bossEntity.getPrice();

                if (itemStack == null) {
                    itemStack = new ItemStack(Material.BARRIER);
                }

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);
                replaceMap.put("{price}", NumberUtils.get().formatDouble(price));

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Shop.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getDisplay().getStringList("Display.Shop.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, e -> {
                    ItemStack spawnItem = this.bossEntityManager.getSpawnItem(bossEntity);
                    Player player = (Player) e.getWhoClicked();

                    if (spawnItem == null) {
                        Debug.FAILED_TO_GIVE_SPAWN_EGG.debug(e.getWhoClicked().getName(), name);
                        return;
                    }


                    if (EconomyManager.hasBalance(player, price)) {
                        Message.Boss_Shop_NotEnoughBalance.msg(player, NumberUtils.get().formatDouble(price));
                        return;
                    }

                    EconomyManager.withdrawBalance(player, price);
                    player.getInventory().addItem(spawnItem);
                    Message.Boss_Shop_Purchased.msg(player, spawnItem.getItemMeta().getDisplayName());
                });
            }
        }));
    }

    private Map<String, BossEntity> getFilteredMap(Map<String, BossEntity> originalMap) {
        Map<String, BossEntity> newMap = new HashMap<>();

        originalMap.forEach((s, bossEntity) -> {
            if (bossEntity.canBeBought()) {
                newMap.put(s, bossEntity);
            }
        });

        return newMap;
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
