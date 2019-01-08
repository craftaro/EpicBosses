package com.songoda.epicbosses.panel.autospawns;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
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
 * @since 07-Jan-19
 */
public class AutoSpawnEntitiesEditorPanel extends VariablePanelHandler<AutoSpawn> {

    private AutoSpawnFileManager autoSpawnFileManager;
    private BossesFileManager bossesFileManager;
    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public AutoSpawnEntitiesEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemsFileManager = plugin.getItemStackManager();
        this.bossesFileManager = plugin.getBossesFileManager();
        this.autoSpawnFileManager = plugin.getAutoSpawnFileManager();
    }

    @Override
    public void fillPanel(Panel panel, AutoSpawn autoSpawn) {
        Map<String, BossEntity> currentEntities = this.bossesFileManager.getBossEntitiesMap();
        List<String> entryList = new ArrayList<>(currentEntities.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentEntities, entryList, autoSpawn);
            return true;
        }));

        loadPage(panel, 0, currentEntities, entryList, autoSpawn);
    }

    @Override
    public void openFor(Player player, AutoSpawn autoSpawn) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getAutoSpawnName(autoSpawn));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainAutoSpawnEditPanel(), autoSpawn);

        fillPanel(panel, autoSpawn);
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, Map<String, BossEntity> currentEntities, List<String> entryList, AutoSpawn autoSpawn) {
        List<String> current = ObjectUtils.getValue(autoSpawn.getEntities(), new ArrayList<>());

        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= entryList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                String name = entryList.get(slot);
                BossEntity bossEntity = currentEntities.get(name);
                ItemStackHolder itemStackHolder = this.itemsFileManager.getItemStackHolder("DefaultMinionMenuSpawnItem");
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);
                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);
                replaceMap.put("{editing}", ""+bossEntity.isEditing());
                replaceMap.put("{targeting}", bossEntity.getTargeting());
                replaceMap.put("{dropTable}", bossEntity.getDrops().getDropTable());

                if(current.contains(name)) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.AutoSpawns.Entities.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.AutoSpawns.Entities.name"), replaceMap);
                }

                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.AutoSpawns.Entities.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, event -> {
                    if(!autoSpawn.isEditing()) {
                        Message.Boss_AutoSpawn_MustToggleEditing.msg(event.getWhoClicked());
                        return;
                    }

                    if(current.contains(name)) {
                        current.remove(name);
                    } else {
                        current.add(name);
                    }

                    autoSpawn.setEntities(current);
                    this.autoSpawnFileManager.save();

                    loadPage(panel, page, currentEntities, entryList, autoSpawn);
                });
            }
        });
    }
}
