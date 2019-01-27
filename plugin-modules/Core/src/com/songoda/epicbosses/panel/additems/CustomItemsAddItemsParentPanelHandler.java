package com.songoda.epicbosses.panel.additems;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.additems.interfaces.IParentPanelHandler;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jan-19
 */
public class CustomItemsAddItemsParentPanelHandler implements IParentPanelHandler {

    private BossPanelManager bossPanelManager;

    public CustomItemsAddItemsParentPanelHandler(BossPanelManager bossPanelManager) {
        this.bossPanelManager = bossPanelManager;
    }

    @Override
    public void openParentPanel(Player player) {
        this.bossPanelManager.getCustomItems().openFor(player);
    }
}
