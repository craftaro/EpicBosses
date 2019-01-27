package com.songoda.epicbosses.panel.additems;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.additems.interfaces.IParentPanelHandler;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jan-19
 */
public class SpawnItemAddItemsParentPanelHandler implements IParentPanelHandler {

    private BossPanelManager bossPanelManager;
    private BossEntity bossEntity;

    public SpawnItemAddItemsParentPanelHandler(BossPanelManager bossPanelManager, BossEntity bossEntity) {
        this.bossPanelManager = bossPanelManager;
        this.bossEntity = bossEntity;
    }

    @Override
    public void openParentPanel(Player player) {
        this.bossPanelManager.getSpawnItemEditMenu().openFor(player, this.bossEntity);
    }
}
