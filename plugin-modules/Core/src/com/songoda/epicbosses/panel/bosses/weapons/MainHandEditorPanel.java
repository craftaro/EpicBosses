package com.songoda.epicbosses.panel.bosses.weapons;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.handlers.ItemStackSubListPanelHandler;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Nov-18
 */
public class MainHandEditorPanel extends ItemStackSubListPanelHandler {

    public MainHandEditorPanel(BossPanelManager bossPanelManager, ConfigurationSection configurationSection, CustomBosses plugin) {
        super(bossPanelManager, configurationSection, plugin);
    }

    @Override
    public Map<String, ItemStackHolder> getFilteredMap(Map<String, ItemStackHolder> originalMap) {
        return originalMap;
    }

    @Override
    public ISubVariablePanelHandler<BossEntity, EntityStatsElement> getParentHolder() {
        return this.bossPanelManager.getEquipmentEditMenu();
    }

    @Override
    public void getUpdateAction(EntityStatsElement entityStatsElement, String newName) {
        entityStatsElement.getHands().setMainHand(newName);
    }

    @Override
    public String getCurrent(EntityStatsElement entityStatsElement) {
        return entityStatsElement.getHands().getMainHand();
    }
}
