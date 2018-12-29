package com.songoda.epicbosses.panel.droptables.types.drop;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 29-Dec-18
 */
public class DropNewRewardEditorPanel extends SubVariablePanelHandler<DropTable, DropTableElement> {

    public DropNewRewardEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, DropTableElement dropTableElement) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable, DropTableElement dropTableElement) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
