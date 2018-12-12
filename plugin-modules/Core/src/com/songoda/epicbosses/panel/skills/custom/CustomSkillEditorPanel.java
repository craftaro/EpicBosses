package com.songoda.epicbosses.panel.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Dec-18
 */
public class CustomSkillEditorPanel extends VariablePanelHandler<Skill> {

    private SkillsFileManager skillsFileManager;
    private BossSkillManager bossSkillManager;
    private CustomBosses plugin;

    public CustomSkillEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.bossSkillManager = plugin.getBossSkillManager();
        this.skillsFileManager = plugin.getSkillsFileManager();
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
