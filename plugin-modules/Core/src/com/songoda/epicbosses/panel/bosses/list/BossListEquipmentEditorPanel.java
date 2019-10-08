package com.songoda.epicbosses.panel.bosses.list;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.bosses.BossListEditorPanel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public class BossListEquipmentEditorPanel extends BossListEditorPanel {

    public BossListEquipmentEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin, "Equipment");
    }

    @Override
    public ClickAction getAction(BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        return event -> this.bossPanelManager.getEquipmentEditMenu().openFor((Player) event.getWhoClicked(), bossEntity, entityStatsElement);
    }
}