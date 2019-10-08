package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.EntityFinder;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Nov-18
 */
public class EntityTypeEditorPanel extends SubVariablePanelHandler<BossEntity, EntityStatsElement> {

    private EpicBosses plugin;

    public EntityTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        List<EntityType> list = ItemStackUtils.getSpawnableEntityTypes();
        int maxPage = panel.getMaxPage(ItemStackUtils.getSpawnableEntityTypes());

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if (requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, list, bossEntity, entityStatsElement);
            return true;
        }));

        loadPage(panel, 0, list, bossEntity, entityStatsElement);
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

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel, bossEntity, entityStatsElement));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int requestedPage, List<EntityType> filteredList, BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        String entityTypeValue = entityStatsElement.getMainStats().getEntityType();

        ServerUtils.get().runTaskAsync(() -> panel.loadPage(requestedPage, ((slot, realisticSlot) -> {
            if (slot >= filteredList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {
                });
            } else {
                EntityType entityType = filteredList.get(slot);
                ItemStack itemStack = ItemStackUtils.getSpawnEggForEntity(entityType);
                Map<String, String> replaceMap = new HashMap<>();
                boolean found = false;

                replaceMap.put("{name}", StringUtils.get().formatString(entityType.name()));

                if (entityTypeValue != null) {
                    EntityFinder entityFinder = EntityFinder.get(entityTypeValue);

                    if (entityFinder != null) {
                        for (String s : entityFinder.getNames()) {
                            if (s.equalsIgnoreCase(entityType.name())) {
                                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Boss.EntityType.selectedName"), replaceMap);
                                found = true;
                                break;
                            }
                        }
                    }
                }

                if (!found) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Boss.EntityType.name"), replaceMap);
                }

                panel.setItem(realisticSlot, itemStack, e -> {
                    EntityFinder entityFinder = EntityFinder.get(entityType.name());

                    if (entityFinder != null) {
                        Message.Boss_Statistics_SetEntityFinder.msg(e.getWhoClicked(), entityFinder.getFancyName());
                        entityStatsElement.getMainStats().setEntityType(entityFinder.getFancyName());
                        this.plugin.getBossesFileManager().save();

                        loadPage(panel, requestedPage, filteredList, bossEntity, entityStatsElement);
                    }
                });
            }
        })));
    }
}
