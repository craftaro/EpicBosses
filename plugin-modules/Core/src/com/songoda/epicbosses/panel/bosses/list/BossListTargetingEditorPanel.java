package com.songoda.epicbosses.panel.bosses.list;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.bosses.BossListEditorPanel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 22-Nov-18
 */
public class BossListTargetingEditorPanel extends BossListEditorPanel {

    public BossListTargetingEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin, "Targeting");
    }

    @Override
    public ClickAction getAction(BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        return event -> {
            ClickType click = event.getClick();
            Player player = (Player) event.getWhoClicked();

            if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT) {
                this.bossPanelManager.getTargetingEditMenu().openFor(player, bossEntity);
            } else {
                if(entityStatsElement.getMainStats().getPosition() > 1) {
                    bossEntity.getEntityStats().remove(entityStatsElement);
                    this.bossesFileManager.save();

                    openFor(player, bossEntity);
                }
            }
        };
    }
}
