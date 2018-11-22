package com.songoda.epicbosses.panel.bosses.list;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.EquipmentElement;
import com.songoda.epicbosses.entity.elements.HandsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.panel.bosses.BossListEditorPanel;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public class BossListEquipmentEditorPanel extends BossListEditorPanel {

    public BossListEquipmentEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin, "Equipment");
    }

    @Override
    public ClickAction getAction(BossEntity bossEntity, EntityStatsElement entityStatsElement) {
        return event -> {
            ClickType click = event.getClick();
            Player player = (Player) event.getWhoClicked();

            if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT) {
                this.bossPanelManager.getEquipmentEditMenu().openFor(player, bossEntity, entityStatsElement);
            } else {
                if (entityStatsElement.getMainStats().getPosition() > 1) {
                    bossEntity.getEntityStats().remove(entityStatsElement);
                    this.bossesFileManager.save();

                    openFor(player, bossEntity);
                }
            }
        };
    }
}