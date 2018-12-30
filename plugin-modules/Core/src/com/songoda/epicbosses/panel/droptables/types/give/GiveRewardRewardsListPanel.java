package com.songoda.epicbosses.panel.droptables.types.give;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.base.handlers.SubSubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 31-Dec-18
 */
public class GiveRewardRewardsListPanel extends SubSubVariablePanelHandler<DropTable, GiveTableElement, String> {

    private CustomBosses plugin;

    public GiveRewardRewardsListPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveTableElement giveTableElement, String s) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
