package com.songoda.epicbosses.panel.additems;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.panel.additems.interfaces.IParentPanelHandler;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jan-19
 */
public class ItemStackSubListParentPanelHandler implements IParentPanelHandler {

    private ISubVariablePanelHandler<BossEntity, EntityStatsElement> panelHandler;
    private EntityStatsElement entityStatsElement;
    private BossEntity bossEntity;

    public ItemStackSubListParentPanelHandler(BossEntity bossEntity, EntityStatsElement entityStatsElement, ISubVariablePanelHandler<BossEntity, EntityStatsElement> panelHandler) {
        this.entityStatsElement = entityStatsElement;
        this.panelHandler = panelHandler;
        this.bossEntity = bossEntity;
    }

    @Override
    public void openParentPanel(Player player) {
        this.panelHandler.openFor(player, this.bossEntity, this.entityStatsElement);
    }
}
