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
 * @since 26-Nov-18
 */
public class BossListStatisticEditorPanel extends BossListEditorPanel {

    public BossListStatisticEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin, "Statistics");
    }

    @Override
    public ClickAction getAction(BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        return event -> this.bossPanelManager.getStatisticMainEditMenu().openFor((Player) event.getWhoClicked(), bossEntity, entityStatsElement);
    }
}
