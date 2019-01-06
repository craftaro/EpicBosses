package com.songoda.epicbosses.panel.autospawns;

import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class MainAutoSpawnEditorPanel extends VariablePanelHandler<AutoSpawn> {

    public MainAutoSpawnEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, AutoSpawn autoSpawn) {

    }

    @Override
    public void openFor(Player player, AutoSpawn autoSpawn) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
