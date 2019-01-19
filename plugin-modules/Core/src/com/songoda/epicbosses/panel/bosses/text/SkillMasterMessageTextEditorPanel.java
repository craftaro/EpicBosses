package com.songoda.epicbosses.panel.bosses.text;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.handlers.SingleMessageListEditor;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 20-Jan-19
 */
public class SkillMasterMessageTextEditorPanel extends SingleMessageListEditor<BossEntity> {

    public SkillMasterMessageTextEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder, plugin);
    }

    @Override
    public String getCurrent(BossEntity object) {
        return object.getSkills().getMasterMessage();
    }

    @Override
    public void updateMessage(BossEntity object, String newPath) {
        object.getSkills().setMasterMessage(newPath);
    }

    @Override
    public IVariablePanelHandler<BossEntity> getParentHolder() {
        return this.bossPanelManager.getSkillsBossEditMenu();
    }

    @Override
    public String getName(BossEntity object) {
        return BossAPI.getBossEntityName(object);
    }
}
