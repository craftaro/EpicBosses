package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.*;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Nov-18
 */
public class EntityTypeEditorPanel extends SubVariablePanelHandler<BossEntity, EntityStatsElement> {

    private CustomBosses plugin;

    public EntityTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        List<EntityType> list = Arrays.stream(EntityType.values()).filter(EntityType::isSpawnable).collect(Collectors.toList());
        Map<EntityType, Material> filteredMap = getFilteredMap(list);
        List<EntityType> filteredList = new ArrayList<>(filteredMap.keySet());
        int maxPage = panel.getMaxPage(filteredMap);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, filteredMap, filteredList, bossEntity, entityStatsElement);
            return true;
        }));

        loadPage(panel, 0, filteredMap, filteredList, bossEntity, entityStatsElement);
    }

    @Override
    public void openFor(Player player, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getStatisticMainEditMenu(), bossEntity, entityStatsElement);

        fillPanel(panel, bossEntity, entityStatsElement);

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private Map<EntityType, Material> getFilteredMap(List<EntityType> entityTypes) {
        Map<EntityType, Material> filteredMap = new HashMap<>();

        entityTypes.forEach(entityType -> {
            String materialName = entityType + "_SPAWN_EGG";
            Material material = Material.matchMaterial(materialName);

            if(material == null) return;

            filteredMap.put(entityType, material);
        });

        return filteredMap;
    }

    private void loadPage(Panel panel, int requestedPage, Map<EntityType, Material> filteredMap, List<EntityType> filteredList, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        String entityTypeValue = entityStatsElement.getMainStats().getEntityType();

        ServerUtils.get().runTaskAsync(() -> panel.loadPage(requestedPage, ((slot, realisticSlot) -> {
            if(slot >= filteredMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                EntityType entityType = filteredList.get(slot);
                Material material = filteredMap.get(entityType);
                ItemStack itemStack = new ItemStack(material);

                Map<String, String> replaceMap = new HashMap<>();
                boolean found = false;

                replaceMap.put("{name}", StringUtils.get().formatString(entityType.name()));

                if(entityTypeValue != null) {
                    EntityFinder entityFinder = EntityFinder.get(entityTypeValue);

                    if (entityFinder != null) {
                        for (String s : entityFinder.getNames()) {
                            if (s.equalsIgnoreCase(entityType.name())) {
                                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.EntityType.selectedName"), replaceMap);
                                found = true;
                                break;
                            }
                        }
                    }
                }

                if(!found) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.EntityType.name"), replaceMap);
                }

                panel.setItem(realisticSlot, itemStack, e -> {
                    EntityFinder entityFinder = EntityFinder.get(entityType.name());

                    if(entityFinder != null) {
                        Message.Boss_Statistics_SetEntityFinder.msg(e.getWhoClicked(), entityFinder.getFancyName());
                        entityStatsElement.getMainStats().setEntityType(entityFinder.getFancyName());
                        this.plugin.getBossesFileManager().save();

                        loadPage(panel, requestedPage, filteredMap, filteredList, bossEntity, entityStatsElement);
                    }
                });
            }
        })));
    }
}
