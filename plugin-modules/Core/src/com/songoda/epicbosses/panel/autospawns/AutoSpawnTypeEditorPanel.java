package com.songoda.epicbosses.panel.autospawns;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class AutoSpawnTypeEditorPanel extends VariablePanelHandler<AutoSpawn> {

    private AutoSpawnFileManager autoSpawnFileManager;

    public AutoSpawnTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.autoSpawnFileManager = plugin.getAutoSpawnFileManager();
    }

    @Override
    public void fillPanel(Panel panel, AutoSpawn autoSpawn) {

    }

    @Override
    public void openFor(Player player, AutoSpawn autoSpawn) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getAutoSpawnName(autoSpawn));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainAutoSpawnEditPanel(), autoSpawn);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        counter.getSlotsWith("IntervalSystem").forEach(slot -> panel.setOnClick(slot, getIntervalSystem(autoSpawn)));
        counter.getSlotsWith("WildernessSystem");
        counter.getSlotsWith("BiomeSystem");
        counter.getSlotsWith("SpawnerSystem");

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getIntervalSystem(AutoSpawn autoSpawn) {
        return event -> {
            if(!autoSpawn.isEditing()) {
                Message.Boss_AutoSpawn_MustToggleEditing.msg(event.getWhoClicked());
                return;
            }

            autoSpawn.setCustomData(BossAPI.convertObjectToJsonObject(new IntervalSpawnElement("world,0,100,0", "{boss_" + BossAPI.getAutoSpawnName(autoSpawn) + "}", 30, false)));
            this.autoSpawnFileManager.save();
        };
    }
}
