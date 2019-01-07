package com.songoda.epicbosses.panel.autospawns;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.managers.AutoSpawnManager;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class AutoSpawnCustomSettingsEditorPanel extends VariablePanelHandler<AutoSpawn> {

    private AutoSpawnManager autoSpawnManager;
    private CustomBosses plugin;

    public AutoSpawnCustomSettingsEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.autoSpawnManager = plugin.getAutoSpawnManager();
    }

    @Override
    public void fillPanel(Panel panel, AutoSpawn autoSpawn) {
        String currentType = ObjectUtils.getValue(autoSpawn.getType(), "");

    }

    @Override
    public void openFor(Player player, AutoSpawn autoSpawn) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getAutoSpawnName(autoSpawn));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainAutoSpawnEditPanel(), autoSpawn);

        fillPanel(panel, autoSpawn);
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
