package com.songoda.epicbosses.panel.skills.custom.commands;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.SubCommandSkillElement;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Dec-18
 */
public class ModifyCommandEditorPanel extends SubVariablePanelHandler<Skill, SubCommandSkillElement> {

    public ModifyCommandEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, Skill skill, SubCommandSkillElement subCommandSkillElement) {

    }

    @Override
    public void openFor(Player player, Skill skill, SubCommandSkillElement subCommandSkillElement) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
//        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
    }
}
