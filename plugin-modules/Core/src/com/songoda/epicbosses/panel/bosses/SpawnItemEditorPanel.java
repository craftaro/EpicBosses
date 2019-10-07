package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.panel.AddItemsPanel;
import com.songoda.epicbosses.panel.additems.SpawnItemAddItemsParentPanelHandler;
import com.songoda.epicbosses.panel.additems.interfaces.IParentPanelHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
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
 * @since 10-Jan-19
 */
public class SpawnItemEditorPanel extends VariablePanelHandler<BossEntity> {

    private BossesFileManager bossesFileManager;
    private ItemsFileManager itemsFileManager;
    private EpicBosses plugin;

    public SpawnItemEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = plugin.getBossesFileManager();
        this.itemsFileManager = plugin.getItemStackManager();
        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {
        Map<String, ItemStackHolder> itemStackHolderMap = this.itemsFileManager.getItemStackHolders();
        List<String> entryList = new ArrayList<>(itemStackHolderMap.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if (requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, itemStackHolderMap, entryList, bossEntity);
            return true;
        }));

        loadPage(panel, 0, itemStackHolderMap, entryList, bossEntity);
    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainBossEditMenu(), bossEntity);

        ServerUtils.get().runTaskAsync(() -> {
            panelBuilderCounter.getSlotsWith("AddNew").forEach(slot -> panel.setOnClick(slot, event -> openAddItemsPanel(player, bossEntity)));
            panelBuilderCounter.getSlotsWith("Remove").forEach(slot -> panel.setOnClick(slot, event -> {
                if (!bossEntity.isEditing()) {
                    Message.Boss_Edit_CannotBeModified.msg(event.getWhoClicked());
                    return;
                }

                bossEntity.setSpawnItem("");
                this.bossesFileManager.save();

                openFor((Player) event.getWhoClicked(), bossEntity);
            }));

            fillPanel(panel, bossEntity);
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void openAddItemsPanel(Player player, BossEntity bossEntity) {
        IParentPanelHandler parentPanelHandler = new SpawnItemAddItemsParentPanelHandler(this.bossPanelManager, bossEntity);
        AddItemsPanel addItemsPanel = new AddItemsPanel(this.bossPanelManager, this.bossPanelManager.getAddItemsBuilder().cloneBuilder(), this.plugin, parentPanelHandler);

        addItemsPanel.openFor(player);
    }

    private void loadPage(Panel panel, int requestedPage, Map<String, ItemStackHolder> filteredMap, List<String> entryList, BossEntity bossEntity) {
        String current = ObjectUtils.getValue(bossEntity.getSpawnItem(), "");

        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if (slot >= filteredMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {
                });
            } else {
                String name = entryList.get(slot);
                ItemStackHolder itemStackHolder = filteredMap.get(name);
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);

                if (itemStack == null) {
                    itemStack = new ItemStack(Material.BARRIER);
                }

                if (name.equalsIgnoreCase(current)) {
                    Map<String, String> replaceMap = new HashMap<>();

                    replaceMap.put("{name}", ItemStackUtils.getName(itemStack));

                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Boss.Equipment.name"), replaceMap);
                }

                panel.setItem(realisticSlot, itemStack, e -> {
                    if (!bossEntity.isEditing()) {
                        Message.Boss_Edit_CannotBeModified.msg(e.getWhoClicked());
                        return;
                    }

                    bossEntity.setSpawnItem(name);
                    this.bossesFileManager.save();

                    loadPage(panel, requestedPage, filteredMap, entryList, bossEntity);
                });
            }
        });
    }
}
