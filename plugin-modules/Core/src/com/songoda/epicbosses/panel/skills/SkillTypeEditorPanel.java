package com.songoda.epicbosses.panel.skills;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class SkillTypeEditorPanel extends VariablePanelHandler<Skill> {

    public SkillTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, Skill skill) {

    }

    @Override
    public void openFor(Player player, Skill skill) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
