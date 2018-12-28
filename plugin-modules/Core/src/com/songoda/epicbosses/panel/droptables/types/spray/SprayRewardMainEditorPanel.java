package com.songoda.epicbosses.panel.droptables.types.spray;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.base.handlers.SubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Dec-18
 */
public class SprayRewardMainEditorPanel extends SubSubVariablePanelHandler<DropTable, SprayTableElement, String> {

    public SprayRewardMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void openFor(Player player, DropTable dropTable, SprayTableElement sprayTableElement, String string) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
