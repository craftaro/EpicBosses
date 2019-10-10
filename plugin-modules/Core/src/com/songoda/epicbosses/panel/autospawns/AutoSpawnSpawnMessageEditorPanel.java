package com.songoda.epicbosses.panel.autospawns;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.panel.handlers.SingleMessageListEditor;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 09-Jan-19
 */
public class AutoSpawnSpawnMessageEditorPanel extends SingleMessageListEditor<AutoSpawn> {

    private AutoSpawnFileManager autoSpawnFileManager;

    public AutoSpawnSpawnMessageEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);

        this.autoSpawnFileManager = plugin.getAutoSpawnFileManager();
    }

    @Override
    public String getCurrent(AutoSpawn object) {
        return ObjectUtils.getValue(object.getAutoSpawnSettings().getSpawnMessage(), "");
    }

    @Override
    public void updateMessage(AutoSpawn object, String newPath) {
        object.getAutoSpawnSettings().setSpawnMessage(newPath);
        this.autoSpawnFileManager.save();
    }

    @Override
    public IVariablePanelHandler<AutoSpawn> getParentHolder() {
        return this.bossPanelManager.getAutoSpawnSpecialSettingsEditorPanel();
    }

    @Override
    public String getName(AutoSpawn object) {
        return BossAPI.getAutoSpawnName(object);
    }
}
