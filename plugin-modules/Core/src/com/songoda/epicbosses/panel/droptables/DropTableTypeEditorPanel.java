package com.songoda.epicbosses.panel.droptables;

import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 23-Dec-18
 */
public class DropTableTypeEditorPanel extends VariablePanelHandler<DropTable> {

    public DropTableTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
