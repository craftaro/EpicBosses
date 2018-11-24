package com.songoda.epicbosses.panel.bosses.equipment;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.handlers.ItemStackSubListPanelHandler;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public class ChestplateEditorPanel extends ItemStackSubListPanelHandler {

    public ChestplateEditorPanel(BossPanelManager bossPanelManager, ConfigurationSection configurationSection, CustomBosses plugin) {
        super(bossPanelManager, configurationSection, plugin);
    }

    @Override
    public Map<String, ItemStackHolder> getFilteredMap(Map<String, ItemStackHolder> originalMap) {
        Map<String, ItemStackHolder> newMap = new HashMap<>();

        originalMap.forEach((string, holder) -> {
            ItemStack itemStack = this.itemStackConverter.from(holder);

            if(itemStack.getType().name().contains("CHESTPLATE")) {
                newMap.put(string, holder);
            }
        });

        return newMap;
    }

    @Override
    public ISubVariablePanelHandler<BossEntity, EntityStatsElement> getParentHolder() {
        return this.bossPanelManager.getEquipmentEditMenu();
    }

    @Override
    public void getUpdateAction(EntityStatsElement entityStatsElement, String newName) {
        entityStatsElement.getEquipment().setChestplate(newName);
    }

    @Override
    public String getCurrent(EntityStatsElement entityStatsElement) {
        return entityStatsElement.getEquipment().getChestplate();
    }
}