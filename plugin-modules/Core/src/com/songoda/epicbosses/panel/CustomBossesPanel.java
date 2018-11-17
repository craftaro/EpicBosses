package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
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
public class CustomBossesPanel extends PanelHandler {

    private BossEntityManager bossEntityManager;
    private BossesFileManager bossesFileManager;
    private CustomBosses customBosses;

    public CustomBossesPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses customBosses) {
        super(bossPanelManager, panelBuilder);

        this.customBosses = customBosses;
        this.bossEntityManager = customBosses.getBossEntityManager();
        this.bossesFileManager = customBosses.getBossesFileManager();
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    @Override
    public void fillPanel(Panel panel) {
        Map<String, BossEntity> currentEntities = this.bossesFileManager.getBossEntitiesMap();
        List<String> entryList = new ArrayList<>(currentEntities.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentEntities, entryList);
            return true;
        }));

        loadPage(panel, 0, currentEntities, entryList);
    }

    @Override
    public void openFor(Player player) {
        Panel panel = getPanelBuilder().getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanel(this.bossPanelManager.getMainMenu().getPanel());

        fillPanel(panel);

        panel.openFor(player);
    }

    private void loadPage(Panel panel, int page, Map<String, BossEntity> bossEntityMap, List<String> entryList) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= bossEntityMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);

                BossEntity entity = bossEntityMap.get(name);
                ItemStack itemStack = this.bossEntityManager.getDisplaySpawnItem(entity);

                if(itemStack == null) {
                    itemStack = new ItemStack(Material.BARRIER);
                }

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);
                replaceMap.put("{enabled}", ""+entity.isEditing());

                ItemStackUtils.applyDisplayName(itemStack, this.customBosses.getConfig().getString("Display.Bosses.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.customBosses.getConfig().getStringList("Display.Bosses.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, e -> {
                    if(e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {
                        //TODO
                    } else if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
                        ItemStack spawnItem = this.bossEntityManager.getSpawnItem(entity);

                        if(spawnItem == null) {
                            Debug.FAILED_TO_GIVE_SPAWN_EGG.debug(e.getWhoClicked().getName(), name);
                            return;
                        }

                        e.getWhoClicked().getInventory().addItem(spawnItem);
                    }
                });
            }
        });
    }
}
